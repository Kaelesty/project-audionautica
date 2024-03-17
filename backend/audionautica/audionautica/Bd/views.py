import os, json, bcrypt, hashlib
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, generics
from rest_framework.exceptions import AuthenticationFailed
from .models import Users, Traks, PlayList
from .serializers import (
    UsersSerializer,
    RegisterSerializer,
    LoginSerialiaer,
    TokenSerializer,
    TrackUploadSerializer,
    FilepathSerializer,
    PlaylistUploadSerializer,
)
import jwt, datetime
from django.http import HttpResponse, HttpResponseNotFound, FileResponse


file_path = "C:/Users/greg/Desktop/music/"
file_path_posters = "C:/Users/greg/Desktop/posters/"
secret_key = "secretkey"
def create_playlist(x):
    playlist = PlayList()
    playlist.title = 'Favorite Playlist'
    playlist.creatorid = str(x)
    playlist.isprivat = 'True'
    playlist.image = file_path_posters + "kitty.jpg"
    playlist.save()
    return playlist.id

def make_hash(password):
    password_bytes = password.encode('utf-8')
    hash_object = hashlib.sha256(password_bytes)
    return hash_object.hexdigest()

def check_token(request):
    token = request.data['token']
    try:
        jwt.decode(token, secret_key, algorithms=['HS256'])
        return True
    except Exception:
        return False
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
            login = serializer['login']
            user = Users.objects.filter(login=login.value).first()
            create_playlist(user.id)
            return Response(serializer.data)
        return Response(serializer.errors)

class LoginUser(APIView):
    def post(self, request, format = None):
        login = request.data['login']
        password = request.data['password']
        hash_password = make_hash(password)
        user = Users.objects.filter(login=login).first()
        if user is None:
            return Response(status=status.HTTP_400_BAD_REQUEST)

        if user.password != hash_password:
            return Response(status=status.HTTP_401_UNAUTHORIZED)

        payload = {
            'id':user.id,
            'exp':datetime.datetime.utcnow()+datetime.timedelta(minutes=1440),
            'iat':datetime.datetime.utcnow()
        }
        token = jwt.encode(payload,secret_key,algorithm='HS256')
        res = {'token':token}
        response = Response(res)

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
            payload = jwt.decode(token,secret_key,algorithms=['HS256'])
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
        if not check_token(request):
            return Response(status = status.HTTP_418_IM_A_TEAPOT)
        track = Traks.objects.filter(id = request.data['id']).first()
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
            if not check_token(request):
                return Response(status=status.HTTP_418_IM_A_TEAPOT)
            file = request.body
            if Traks.objects.filter(title=meta_data[0], artist=meta_data[1]):
                return Response(status=status.HTTP_400_BAD_REQUEST)
            else:
                track = Traks()
                track.title = request.headers["title"]
                track.artist = request.headers["artist"]
                track.tags = request.headers["tags"]
                track.filepath =""
                track.save()
                track.filepath = file_path+ str(track.id) +'.mp3'
                track.save()
                with open(track.filepath, 'wb') as f:
                    f.write(file)
                f.close()
        return Response(track.id, status=status.HTTP_200_OK)

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
        if not check_token(request):
            return Response(status=status.HTTP_418_IM_A_TEAPOT)
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
        if not check_token(request):
            return Response(status=status.HTTP_418_IM_A_TEAPOT)
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
class PlayListUpload(APIView):
    def post(self, request):
        if request.method == "POST":
            if not check_token(request):
                return Response(status=status.HTTP_418_IM_A_TEAPOT)
            image = request.body
            ext = request.headers["ext"]
            playlist = PlayList()
            playlist.title = request.headers["title"]
            playlist.creatorid = request.headers["creatorid"]
            playlist.description = request.headers["description"]
            playlist.tracksid = request.headers["tracksid"]
            playlist.isprivat = request.headers["isprivat"]
            playlist.save()
            playlist.image = file_path_posters+ str(playlist.id) +ext
            playlist.save()
            with open(playlist.image, 'wb') as f:
                f.write(image)
            f.close()
            return Response(playlist.id, status=status.HTTP_200_OK)

class PlayListDeleteId(APIView):
    def post(self, request):
        if not check_token(request):
            return Response(status=status.HTTP_418_IM_A_TEAPOT)
        try:
            id = request.data["id"]
            playlist = PlayList.objects.get(id = id)
            if not playlist.image == file_path_posters + "kitty.jpg":
                os.remove(playlist.image)
            playlist.delete()
            return Response(status = status.HTTP_200_OK)
        except:
            return Response(status=status.HTTP_400_BAD_REQUEST)

class GetPlayListPoster(APIView):
    def post(self, request):
        if request.method == "POST":
            if not check_token(request):
                return Response(status=status.HTTP_418_IM_A_TEAPOT)
            playlist_id = request.data["playlist_id"]
            playlist = PlayList.objects.get(id=playlist_id)
            path = playlist.image
            print(path)
            return FileResponse(open(path,'rb'))

class AddTrackToPlayList(APIView):
    def post(self, request):
        if request.method == "POST":
            if not check_token(request):
                return Response(status=status.HTTP_418_IM_A_TEAPOT)
            playlist_id = request.data["playlist_id"]
            track_id = request.data["track_id"]
            playlist = PlayList.objects.filter(id = playlist_id).first()
            tracks = playlist.tracksid
            tracks += track_id
            tracks += "/"
            playlist.tracksid = tracks
            playlist.save()
            return Response(status = status.HTTP_200_OK)

class DeleteTrackFromPlayList(APIView):
    def post(self, request):
        if not check_token(request):
            return Response(status=status.HTTP_418_IM_A_TEAPOT)
        if request.method == "POST":
            playlist_id = request.data["playlist_id"]
            track_id = request.data["track_id"]
            playlist = PlayList.objects.filter(id = playlist_id).first()
            tracks = playlist.tracksid
            tracks = "/".join(filter(lambda x: x != track_id, tracks.split("/")))
            playlist.tracksid = tracks
            playlist.save()
            return Response(status = status.HTTP_200_OK)

class LikePlayList(APIView):
    def post(self, request):
        if not check_token(request):
            return Response(status=status.HTTP_418_IM_A_TEAPOT)
        if request.method == "POST":
            token = request.data["token"]
            playlist_id = request.data["playlist_id"]
            payload = jwt.decode(token, secret_key, algorithms=['HS256'])
            user = Users.objects.filter(id = payload['id']).first()
            user_playlists = user.likedplaylist
            user_playlists += playlist_id
            user_playlists += "/"
            user.likedplaylist = user_playlists
            user.save()

            return Response(status=status.HTTP_200_OK)
