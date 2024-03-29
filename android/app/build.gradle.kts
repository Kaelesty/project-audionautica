import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
}

android {
	namespace = "com.kaelesty.audionautica"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.kaelesty.audionautica"
		minSdk = 24
		targetSdk = 33
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.3"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	implementation("androidx.core:core-ktx:1.9.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
	implementation("androidx.activity:activity-compose:1.8.0")
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation("androidx.navigation:navigation-compose:2.7.6")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
	implementation("androidx.compose.material3:material3:1.1.1")
	implementation("androidx.compose.runtime:runtime-livedata:1.5.3")

	implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")
	implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.google.code.gson:gson:2.10.1")

	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

	var room_version = "2.5.2"

	implementation("androidx.room:room-runtime:$room_version")
	annotationProcessor ("androidx.room:room-compiler:$room_version")
	kapt("androidx.room:room-compiler:$room_version")
	implementation ("androidx.room:room-ktx:$room_version")

	implementation("com.google.dagger:dagger:2.47")
	kapt("com.google.dagger:dagger-compiler:2.47")

	implementation("androidx.datastore:datastore-preferences:1.1.0-alpha06")

	implementation("com.google.android.exoplayer:exoplayer:2.19.1")
	implementation("androidx.media3:media3-exoplayer:1.1.1")
	implementation("androidx.media3:media3-exoplayer-dash:1.1.1")
	implementation("androidx.media3:media3-ui:1.1.1")

	implementation("io.coil-kt:coil-compose:2.5.0")

	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
	implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
}