class LocalTokenStorage {
    setToken(value) {
        localStorage.setItem("apiToken", value);
    }

    hasToken() {
        return !!this.getToken();
    }

    getToken() {
        return localStorage.getItem("apiToken");
    }

    clearToken() {
        localStorage.removeItem("apiToken");
    }
}

export default new LocalTokenStorage();