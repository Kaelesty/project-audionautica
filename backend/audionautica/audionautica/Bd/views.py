import rest_framework_simplejwt.tokens
from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework import generics
from .models import Users
from .serializers import UsersSerializer, Tokens,RegisterSerializer, LoginSerialiaer
from django.contrib.auth.forms import UserCreationForm
from django import forms
from rest_framework_simplejwt.tokens import RefreshToken
from .models import Users

class UsersAPIViwe(generics.ListAPIView):
    queryset = Users.objects.all()
    serializer_class = UsersSerializer

class CheckConnection(APIView):
    queryset = Users.objects.all()
    serializer_class = UsersSerializer

class LoginUser(APIView):
    def post(self, request, format = None):
        serializer = LoginSerialiaer(data=request.data)
        if serializer.is_valid():
            return Response(Users.objects.all())
        return Response(serializer.errors)

class RegisterUser(APIView):
    def post(self, request, format = None):
        serializer = RegisterSerializer(data = request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return  Response(serializer.errors)


