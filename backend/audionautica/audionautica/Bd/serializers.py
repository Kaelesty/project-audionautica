from rest_framework import serializers
from django.contrib.auth.hashers import make_password
from .models import Users, Traks, PlayList
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
    TokenVerifyView
)

class UsersSerializer(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields = ('name','login')
class RegisterSerializer(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields = ('login', 'name','password')
        #to hide field password
        # extra_kwargs = {
        #     'password' : {'write_only':True}
        # }
    def create(self, validated_data):
        password = validated_data["password"]#.pop('password', None)
        hash_password = hash(password)
        validated_data["password"] = hash_password
        instance = self.Meta.model(**validated_data)
        if password is not None:
            Users.password = make_password('password')
        instance.save()
        return instance

class LoginSerialiaer(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields = ('Login', 'Password')
class TokenSerializer(TokenObtainPairView):
    class Meta:
        model = Users
        fields = ('Login', 'Password')


class FilepathSerializer(serializers.ModelSerializer):
    class Meta:
        model = Traks
        fields = ('title', 'filepath')

class TrackUploadSerializer(serializers.ModelSerializer):
    class Meta:
        model = Traks
        fields = ('title','artist','filepath')

class PlaylistUploadSerializer(serializers.ModelSerializer):

    class Meta:
        model = PlayList
        fields = ('title','creatorid','description','tracksid','isprivat','image')
