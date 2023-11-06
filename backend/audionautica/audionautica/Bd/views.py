import rest_framework_simplejwt.tokens
from django.contrib.auth.hashers import check_password
from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, generics
from rest_framework.exceptions import AuthenticationFailed
from .models import Users
from .serializers import UsersSerializer, RegisterSerializer, LoginSerialiaer, TokenSerializer
import jwt, datetime

class UsersAPIViwe(generics.ListAPIView):
    queryset = Users.objects.all()
    serializer_class = UsersSerializer

class TokenViwe(APIView):
    queryset = Users.objects.all()
    serializer_class = TokenSerializer
class CheckConnection(APIView):
    def get(self, request):
        return Response()

class RegisterUser(APIView):
    def post(self, request, format=None):
        serializer = RegisterSerializer(data=request.data)
        print(request.data)
        if Users.objects.filter(login=request.data['login']):
            return Response(status=status.HTTP_400_BAD_REQUEST)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors)

class LoginUser(APIView):
    def post(self, request, format = None):
        login = request.data['login']
        password = request.data['password']

        user = Users.objects.filter(login=login).first()
        user_pass = Users.objects.filter(password=password).first()
        if user is None:
            return Response(status=status.HTTP_400_BAD_REQUEST)

        if user_pass is None:
            return Response(status=status.HTTP_401_UNAUTHORIZED)

        payload = {
            'id':user.id,
            'exp':datetime.datetime.utcnow()+datetime.timedelta(minutes=1440),
            'iat':datetime.datetime.utcnow()
        }
        token = jwt.encode(payload,'secret',algorithm='HS256')

        response = Response(token)

        #from str to cookie
        # response.set_cookie(key='jwt', value=token,httponly=True)
        # response.data = {
        #     'jwt':token
        # }

        return response

class UserView(APIView):
    def post(self, request):
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

# class LogoutView(APIView):
#     def post(self, request):
#         response = Response()
#         #response.delete_cookie('jwt')
#         response.data = {
#             'message':'success'
#         }
#         return response