import os
from time import sleep
while True:
    os.system("git add .")
    os.system('git commit -m "Server commit"')
    os.system("git pull")
    sleep(6*60*60)