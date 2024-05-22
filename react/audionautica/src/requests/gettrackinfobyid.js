import BackURL from "../urls";
import token from "../token";

async function getTrackInfoById(id) {
    try {
        const response = await fetch(BackURL + "/Music/TrackInfoId/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': token.getToken()
            },
            body: JSON.stringify({id}),
        });
        
        if (response.status === 404) {
            console.error('Трек не найден');
            return null;
        }
        //console.log("Запрос на получение информации о треке");
        const result = await response.json()
        return result
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default getTrackInfoById