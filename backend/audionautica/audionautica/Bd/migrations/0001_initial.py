# Generated by Django 4.2.6 on 2024-03-11 18:27

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Traks',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=250, verbose_name='title')),
                ('artist', models.CharField(max_length=250, verbose_name='artist')),
                ('filepath', models.CharField(max_length=1000, verbose_name='filepath')),
                ('tags', models.CharField(max_length=1000, null=True, verbose_name='tags')),
            ],
            options={
                'verbose_name': 'Trak',
            },
        ),
        migrations.CreateModel(
            name='Users',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=250, verbose_name='name')),
                ('login', models.CharField(max_length=250, null=True, unique=True, verbose_name='login')),
                ('password', models.CharField(max_length=250, verbose_name='password')),
            ],
            options={
                'verbose_name': 'User',
            },
        ),
    ]
