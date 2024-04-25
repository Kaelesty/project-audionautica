from django.db import models
from datetime import datetime

class Users(models.Model):
    name = models.CharField("name", max_length= 240)
    login = models.CharField("login", max_length=240, unique=True, null = True)
    password = models.CharField("password", max_length=240)
    likedplaylist = models.CharField("likedplaylist", max_length=240)
    lastupdate_date = models.DateTimeField("lastupdate_date", default=datetime.now())

    def __str__(self):
        return self.name +' id {}'.format(self.id)

    class Meta:
        verbose_name = 'User'

class Traks(models.Model):
    title = models.CharField("title", max_length=240)
    artist = models.CharField("artist", max_length= 240)
    filepath = models.CharField("filepath", max_length= 1100)
    tags = models.CharField("tags", max_length= 1100, null= True)

    def __str__(self):
        return self.title +' id {}'.format(self.id)

    class Meta:
        verbose_name = "Trak"

class PlayList(models.Model):
    title = models.CharField("title", max_length=240)
    creatorid = models.CharField("CreatorId", max_length=240)
    image = models.CharField("image_url", max_length=240)
    description = models.CharField("description", max_length=240)
    tracksid = models.CharField("tracksid", max_length=240)
    liked = models.CharField("liked", max_length=240)
    isprivat = models.CharField("isprivat", max_length=210)

    def __str__(self):
        return self.title +' id {}'.format(self.id)

    class Meta:
        verbose_name = "PlayList"

class Recommendations(models.Model):
    track_id = models.CharField("track_id", max_length=240)
    #some fields

    def __str__(self):
        return 'id {}'.format(self.id)

    class Meta:
        verbose_name = "Recommendations"