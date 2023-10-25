from django.shortcuts import render
from rest_framework import generics
from .models import Users
from .serializers import UsersSerializer
from django.contrib.auth.forms import UserCreationForm
from django import forms

class UsersAPIViwe(generics.ListAPIView):
    queryset = Users.objects.all()
    serializer_class = UsersSerializer
    http_method_names = ['get']

class RegisterUser(UserCreationForm):
    email = forms.EmailField(label="Login")
    fullname = forms.CharField(label="Name")

    class Meta:
        model = Users
        fields = ("Name", "Login")

