from django.db import models

class Users(models.Model):
    name = models.CharField("name", max_length= 250)
    login = models.CharField("login", max_length=250, unique=True)
    password = models.CharField("password", max_length=250)

    def __str__(self):
        return self.name

    class Meta:
        verbose_name = 'User'
