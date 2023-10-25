from django.db import models

class Users(models.Model):
    Name = models.CharField("name", max_length= 250)
    Login = models.CharField("login", max_length=250)
    Password = models.CharField("password", max_length=250)

    def __str__(self):
        return self.Name

    class Meta:
        verbose_name = 'User'