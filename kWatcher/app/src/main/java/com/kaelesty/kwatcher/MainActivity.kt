package com.kaelesty.kwatcher

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.registerForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.MutableLiveData
import com.kaelesty.kwatcher.ui.theme.KWatcherTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.security.Permission
import java.time.Instant

class MainActivity : ComponentActivity() {

	private val scope = CoroutineScope(Dispatchers.IO)

	val PERMISSIONS =
		setOf(
			HealthPermission.getReadPermission(HeartRateRecord::class),
		)

	val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

	val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
		Log.d("kWatcherTag", "Asking Permissions")
		if (granted.containsAll(PERMISSIONS)) {
			Log.d("kWatcherTag", "Granted!")
			readyToRead.postValue(true)
		} else {
			Log.d("kWatcherTag", "Denied!")
		}
	}

	private val readyToRead = MutableLiveData<Boolean>()

	override fun onCreate(savedInstanceState: Bundle?) {

		val availabilityStatus = HealthConnectClient.getSdkStatus(this@MainActivity)
		if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
			Log.d("kWatcherTag", "HCC is not installed")
			return
		}
		val healthConnectClient = HealthConnectClient.getOrCreate(this@MainActivity)
		connectHealthClient(healthConnectClient)

		readyToRead.observe(this@MainActivity) {
			if (it) {
				Log.d("kWatcherTag", "Ready to Read")
				scope.launch {
					readBPM(healthConnectClient,
						Instant.now().minusSeconds(60000),
						Instant.now().plusSeconds(10))
				}
			}
		}

		super.onCreate(savedInstanceState)
		setContent {
			KWatcherTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
				}
			}
		}
	}

	suspend fun readBPM(
		healthConnectClient: HealthConnectClient,
		startTime: Instant,
		endTime: Instant
	) {
		Log.d("kWatcherTag", "Reading")
		try {
			val response =
				healthConnectClient.readRecords(
					ReadRecordsRequest(
						HeartRateRecord::class,
						timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
					)
				)
			Log.d("kWatcherTag", "Records Available: ${response.records.size}")
			for (bpmRecord in response.records) {
				Log.d("kWatcherTag", bpmRecord.toString())
			}
		} catch (e: Exception) {
			Log.d("kWatcherTag", "Reading Failed")
		}
	}


	suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
		val granted = healthConnectClient.permissionController.getGrantedPermissions()
		if (granted.containsAll(PERMISSIONS)) {
			readyToRead.postValue(true)
		} else {
			requestPermissions.launch(PERMISSIONS)
		}
	}



	fun connectHealthClient(healthConnectClient: HealthConnectClient) {

		scope.launch {
			checkPermissionsAndRun(healthConnectClient)
		}

	}

	override fun onDestroy() {
		super.onDestroy()
		scope.cancel()
	}
}