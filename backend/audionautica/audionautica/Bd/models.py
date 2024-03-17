from django.db import models

class Users(models.Model):
    name = models.CharField("name", max_length= 250)
    login = models.CharField("login", max_length=250, unique=True, null = True)
    password = models.CharField("password", max_length=250)
    likedplaylist = models.CharField("likedplaylist", max_length=250)

    def __str__(self):
        return self.name

    class Meta:
        verbose_name = 'User'

class Traks(models.Model):
    title = models.CharField("title", max_length=250)
    artist = models.CharField("artist", max_length= 250)
    filepath = models.CharField("filepath", max_length= 1000)
    tags = models.CharField("tags", max_length= 1000, null= True)

    def __str__(self):
        return self.title +' id {}'.format(self.id)

    class Meta:
        verbose_name = "Trak"

class PlayList(models.Model):
    title = models.CharField("title", max_length=250)
    creatorid = models.CharField("CreatorId", max_length=250)
    image = models.CharField("image_url", max_length=250)
    description = models.CharField("description", max_length=250)
    tracksid = models.CharField("tracksid", max_length=250)
    isprivat = models.CharField("isprivat", max_length=200)

    def __str__(self):
        return self.title +' id {}'.format(self.id)

    class Meta:
        verbose_name = "PlayList"