import BackURL from "../urls";
import token from "../token";

async function getUsernameById(id) {
    try {
        const response = await fetch(BackURL + "/Auth/UserInfoId/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'token': token.getToken()
            },
            body: JSON.stringify({id}),
        });
        
        if (response.status === 404) {
            console.error('Пользователь не найден');
            return null;
        }
        console.log("Запрос на получение данных пользователя");
        const result = await response.json()
        return result.username
    } catch (error) {
        console.error('Ошибка:', error);
        return null;
    }
}

export default getUsernameById