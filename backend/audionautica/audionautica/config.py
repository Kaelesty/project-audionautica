from dataclasses import dataclass

@dataclass
class Config:
    #file_path = "C:/Users/greg//Desktop/music/"
    file_path = "/files/music/"
    file_path_posters = "/home/project-audionautica/backend/audionautica/audionautica/posters/"
    #file_path_posters = "C:/Users/greg/Desktop/project-audionautica/backend/audionautica/audionautica/posters/"
    secret_key = "secretkey"