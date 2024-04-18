import BackURL from "./urls";
import token from "./token";

async function getTrackById(id) {
    try {
        const response = await fetch(BackURL + "/Music/GetTrack/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': token.getToken()
            },
            body: JSON.stringify({id}),
            responseType: 'blob'
        });
        
        if (response.status === 404) {
            console.error('Трек не найден');
            return null;
        }
        
        console.log("Отправлен запрос на получение трека");
        const trackBlob = await response.blob(); // Получаем бинарный трек
        return trackBlob
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default getTrackById