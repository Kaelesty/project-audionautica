from django.contrib import admin
from .models import Users, Traks, PlayList, Recommendations


admin.site.register(Users)
admin.site.register(Traks)
admin.site.register(PlayList)
admin.site.register(Recommendations)