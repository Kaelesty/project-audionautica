import os
import json

from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, generics
from rest_framework.exceptions import AuthenticationFailed
from .models import Users, Traks
from .serializers import UsersSerializer, RegisterSerializer, LoginSerialiaer, TokenSerializer, TrackUploadSerializer, FilepathSerializer
import jwt, datetime
from django.http import HttpResponse, HttpResponseNotFound, FileResponse


file_path = "C:/Users/greg/Desktop/"
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
        #print(request.data)
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
        user_pass = Users.objects.filter(password=hash(password)).first()
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

class GetTrack(APIView):
    def post(self, request):
        id = request.data
        track = Traks.objects.filter(id = id['id']).first()
        serializer = FilepathSerializer(track)
        path = serializer['filepath'].value
        try:
            with open(path, 'rb') as f:
                file_data = f.read()

            # sending response
            # response = FileResponse(file_data)
            response = HttpResponse(file_data, content_type='audio/mpeg')
            #response['Content-Disposition'] = 'attachment; filename="succes.mp3"'

        except IOError:
            # handle file not exist case here
            response = HttpResponseNotFound('<h1>File not exist</h1>')

        return response

class TrackUpload(APIView):
    def post(self, request):
        if request.method == "POST":
            file = request.body
            meta_data = []
            meta_data.append(request.headers["title"])
            meta_data.append(request.headers["artist"])
            meta_data.append(request.headers["tags"])
            print(meta_data)
            if Traks.objects.filter(title=meta_data[0], artist=meta_data[1]):
                return Response(status=status.HTTP_400_BAD_REQUEST)
            else:
                track = Traks()
                track.title = meta_data[0]
                track.artist = meta_data[1]
                track.tags = meta_data[2]
                #replace w to wb
                with open(file_path+meta_data[0]+'.mp3', 'wb') as f:
                    f.write(file)
                f.close()
                track.filepath = file_path+meta_data[0]+'.mp3'
                track.save()
        return Response(track.pk, status=status.HTTP_200_OK)

#rewrite Delete
class TrackDelete(APIView):
    def post(self, request):
        try:
            data = request.data
            meta = data['headers']
            file = data['file']
            meta_data = meta.split('&')
            track = Traks.objects.get(title = meta_data[0], artist = meta_data[1])
            os.remove(track.filepath)
            track.delete()
            return Response(status=status.HTTP_200_OK)
        except:
            return Response(status = status.HTTP_400_BAD_REQUEST)
class TrackDeleteId(APIView):
    def post(self, request):
        try:
            id = request.data["id"]
            print(id)
            track = Traks.objects.get(id = id)
            os.remove(track.filepath)
            track.delete()
            return Response(status = status.HTTP_200_OK)
        except:
            return Response(status=status.HTTP_400_BAD_REQUEST)
class TrackSearch(APIView):
    def post(self, request):
        query = request.data["query"].lower()
        result = Traks.objects.filter(artist=query)
        all_tracks = Traks.objects.all()
        list = []
        response = []
        json_response = {}
        for i in range(Traks.objects.count()):
            res = []
            res.append(all_tracks[i].title)
            res.append(all_tracks[i].artist)
            res.append(all_tracks[i].tags)
            res.append(str(all_tracks[i].pk))
            list.append(res)
        for i in range(len(list)):
            string = ''.join(list[i])
            if query in string:
                response.append(list[i])
        if len(response) == 0:
            return HttpResponse('<h1>Track not exist</h1>')
        else:
            array = []
            for i in range(len(response)):
                track = {}
                track['title'] = response[i][0]
                track['artist'] = response[i][1]
                track['tags'] = response[i][2]
                track['id'] = response[i][3]
                array.append(track)
            print(array)
            json_response['tracks'] = array
        return Response(json_response)
class Test(APIView):
    pass