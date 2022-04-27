import axios from 'axios'

const API_URL = 'http://localhost:8081'

export const USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser'

class AuthenticationService {

    createUser(login, password) {
        return axios.post(API_URL, {
            login,
            password
        });
    }

    executeJwtAuthenticationService(login, password) {
        console.log(login);
        return axios.post(`${API_URL}/auth`, {
            login,
            password
        });
    }

    registerSuccessfulLoginForJwt(login, token) {
        sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, login)
        this.setupAxiosInterceptors(this.createJWTToken(token))
    }

    createJWTToken(token) {
        return 'Bearer ' + token
    }

    logout() {
        sessionStorage.removeItem(USER_NAME_SESSION_ATTRIBUTE_NAME);
    }

    isUserLoggedIn() {
        let user = sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME)
        if (user === null) return false
        return true
    }

    getLoggedInUserName() {
        let user = sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME)
        if (user === null) return ''
        return user
    }

    setupAxiosInterceptors(token) {
        axios.interceptors.request.use(
            (config) => {
                if (this.isUserLoggedIn()) {
                    config.headers.authorization = token
                }
                return config
            }
        )
    }
}

export default new AuthenticationService()