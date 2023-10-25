from rest_framework import serializers
from .models import Users
from rest_framework_simplejwt.tokens import RefreshToken

class UsersSerializer(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields = ('Name','Password')
class RegisterSerializer(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields = ('Login', 'Name','Password')

class LoginSerialiaer(serializers.ModelSerializer):
    class Meta:
        model = Users
        fields = ('Login', 'Password')
class Tokens(serializers.ModelSerializer):

    def get_tokens_for_user(user):
        refresh = RefreshToken.for_user(user)

        return {
            'refresh': str(refresh),
            'access': str(refresh.access_token),
        }

    class Meta:
        model = Users
        fields = '__all__'
