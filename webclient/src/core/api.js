import 'whatwg-fetch'
import localTokenStorage from "./localTokenStorage";

export class Api {
    constructor(tokenStorage = localTokenStorage){
        this.tokenStorage = tokenStorage;
    }

    setToken(value) {
        return this.tokenStorage.setToken(value);
    }

    hasToken() {
        return this.tokenStorage.hasToken();
    }

    getToken() {
        return this.tokenStorage.getToken();
    }

    clearToken() {
        this.tokenStorage.clearToken();
    }

    handleError = async (response) => {
        if (response.status === 401) {
            throw new UnauthenticatedError("");
        }

        if (response.status === 402) {
            throw new UnauthorizedError("");
        }

        if (response.status === 400) {
            const body = await response.json();
            throw new BadRequestError(response.statusText, response.status, body);
        }

        if (response.status === 404) {
            const body = await response.text();
            throw new NotFoundRequestError(response.statusText, response.status, body);
        }

        if (response.status !== 200) {
            const body = await response.text();
            throw new ServerError(response.statusText, response.status, body);
        }

        return response;
    }

    handleCatchError(error) {
        throw error;
    }

    getHeader(noToken) {
        if (this.hasToken() && !noToken) {
            return {
                ...headers,
                'Authorization': 'Bearer ' + this.getToken(),
            }
        }

        return headers;
    }


    get = function (path) {
        return Promise.resolve(getFetch()(path, {
            headers: this.getHeader(),
            credentials: 'same-origin'
        })).then(this.handleError).catch(this.handleCatchError);
    }

    delete = function (path, data) {
        return Promise.resolve(getFetch()(path, {
            method: "DELETE",
            headers: this.getHeader(),
            credentials: 'same-origin',
            body: this.stringifyContent(data)
        })).then(this.handleError).catch(this.handleCatchError)
    }

    put = function (path, data) {
        return Promise.resolve(getFetch()(path, {
            method: "PUT",
            headers: this.getHeader(),
            credentials: 'same-origin',
            body: this.stringifyContent(data)
        })).then(this.handleError).catch(this.handleCatchError)
    }

    post = function (path, data) {
        return Promise.resolve(getFetch()(path, {
            method: "POST",
            headers: this.getHeader(),
            credentials: 'same-origin',
            body: this.stringifyContent(data)
        })).then(this.handleError).catch(this.handleCatchError)
    }

    login = function (path, data) {
        return Promise.resolve(getFetch()(path, {
            method: "POST",
            headers: this.getHeader(true),
            credentials: 'same-origin',
            body: this.stringifyContent(data)
        })).then(this.handleError).catch(this.handleCatchError)
    }

    patch = function (path, data) {
        return Promise.resolve(getFetch()(path, {
            method: "PATCH",
            headers: this.getHeader(),
            credentials: 'same-origin',
            body: this.stringifyContent(data)
        })).then(this.handleError).catch(this.handleCatchError)
    }

    stringifyContent(data) {
        if (!data) return null;
        if (typeof data == "string") {
            return data;
        }

        return JSON.stringify(data);
    }
}

export default new Api();

const headers = {
    'Accept': 'application/json, text/plain, */*',
    'Content-Type': 'application/json'
}

function getFetch() {
    if ((window).customFetch) {
        return (window).customFetch;
    }

    return fetch;
}

export class UnauthorizedError extends Error { }
export class UnauthenticatedError extends Error { }
export class ServerError extends Error {
    constructor(error, statusCode, body) {
        super(error);
        this.body = body;
        this.statusCode = statusCode;
    }
}

export class BadRequestError extends ServerError {

}

export class NotFoundRequestError extends ServerError {

}

