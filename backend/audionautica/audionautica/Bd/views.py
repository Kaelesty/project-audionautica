import rest_framework_simplejwt.tokens
from django.contrib.auth.hashers import check_password
from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, generics
from rest_framework.exceptions import AuthenticationFailed
from .models import Users
from .serializers import UsersSerializer, RegisterSerializer, LoginSerialiaer, TokenSerializer
from django.contrib.auth.forms import UserCreationForm
from django import forms
from rest_framework_simplejwt.tokens import RefreshToken
import jwt, datetime

class UsersAPIViwe(generics.ListAPIView):
    queryset = Users.objects.all()
    serializer_class = UsersSerializer

class TokenViwe(APIView):
    queryset = Users.objects.all()
    serializer_class = TokenSerializer
class CheckConnection(APIView):
    queryset = Users.objects.all()
    serializer_class = UsersSerializer

class RegisterUser(APIView):
    def post(self, request, format = None):
        serializer = RegisterSerializer(data = request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return  Response(serializer.errors)
class LoginUser(APIView):
    def post(self, request, format = None):
        login = request.data['login']
        password = request.data['password']

        user = Users.objects.filter(login = login).first()
        if user is None:
            raise AuthenticationFailed('User not found')

        #need to be fixed
        # if not user.check_password(password):
        #     raise AuthenticationFailed('Incorrect password')

        payload = {
            'id':user.id,
            'exp':datetime.datetime.utcnow()+datetime.timedelta(minutes=60),
            'iat':datetime.datetime.utcnow()
        }
        token = jwt.encode(payload,'secret',algorithm='HS256')

        response = Response(token)

        #from str to cooki
        # response.set_cookie(key='jwt', value=token,httponly=True)
        # response.data = {
        #     'jwt':token
        # }

        return response

class UserView(APIView):
    def get(self, request):
        token = request.data
        #token = request.COOKIES.get('jwt')
        # if not token:
        #     raise AuthenticationFailed('Unauthrnticated')

        try:
            payload = jwt.decode(token,'secret',algorithms=['HS256'])
        except jwt.ExpiredSignatureError:
            raise AuthenticationFailed('Unauthrnticated')

        user = Users.objects.filter(id = payload['id']).first()
        serializer = UsersSerializer(user)
        return Response(serializer.data)

class LogoutView(APIView):
    def post(self, request):
        response = Response()
        response.delete_cookie('jwt')
        response.data = {
            'message':'success'
        }
        return response