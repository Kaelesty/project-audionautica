import BackURL from "../urls";
import token from "../token";

async function getUserPlaylists() {
    try {
        const response = await fetch(BackURL + "/PlayList/UserPlayList/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': token.getToken()
            },       
        });
        
        //console.log("Отправлен запрос на получение плейлистов пользователя");
        const data = await response.json();
        return data // Object { liked: [], created: [] }
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default getUserPlaylists