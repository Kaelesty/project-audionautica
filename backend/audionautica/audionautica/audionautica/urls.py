"""
URL configuration for audionautica project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include, re_path
from Bd.views import (
    UsersAPIViwe,
    RegisterUser,
    CheckConnection,
    LoginUser,
    TokenViwe,
    UserView,
    GetTrack,
    TrackUpload,
    TrackDelete,
    TrackSearch,
    TrackDeleteId,
    PlayListUpload,
    GetPlayListPoster,
    PlayListDeleteId,
    AddTrackToPlayList,
    DeleteTrackFromPlayList,
    LikePlayList,
)
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
    TokenVerifyView
)
from rest_framework import routers

urlpatterns = [
    path('admin/', admin.site.urls),
    path('namelist/', UsersAPIViwe.as_view()),
    path('Auth/CheckConnection/', CheckConnection.as_view()),
    path('Auth/Register/', RegisterUser.as_view()),
    path('Auth/Login/', LoginUser.as_view()),
    path('Auth/User/', UserView.as_view()),
    path('Music/GetTrack/', GetTrack.as_view()),
    path('Music/Upload/', TrackUpload.as_view()),
    path('Music/Delete/', TrackDelete.as_view()),
    path('Music/IdDelete/', TrackDeleteId.as_view()),
    path('Music/Search/', TrackSearch.as_view()),
    path('PlayList/Upload/', PlayListUpload.as_view()),
    path('PlayList/GetPoster/', GetPlayListPoster.as_view()),
    path('PlayList/DeleteId/', PlayListDeleteId.as_view()),
    path('PlayList/AddTrackToPlayList/', AddTrackToPlayList.as_view()),
    path('PlayList/DeleteTrackFromPlayList/', DeleteTrackFromPlayList.as_view()),
    path('PlayList/LikePlayList/', LikePlayList.as_view()),
    #path('Logout/', LogoutView.as_view()),
    # path('token/', TokenObtainPairView.as_view(), name='token_obtain_pair'), #for login give access & refresh token
    # path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'), #refreshing
    # path('token/verify/', TokenVerifyView.as_view(), name = 'token_verify'), #verifykation
    #path('register/', include('djoser.urls')),
    #path('jwt/create', include('djoser.urls')),
    #re_path(r'^auth/', include('djoser.urls.authtoken')),
]
