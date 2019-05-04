import 'whatwg-fetch'
import Promise from "bluebird";

let apiToken = undefined;

export function setToken(value) {
    apiToken = value;
}

export function hasToken(){
    return !!apiToken;
}

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

export class UnauthorizedError extends Error {}

function handleError(response) {
    if (response.status === 401) {
        throw new UnauthorizedError("Unauthorized");
    }

    return response;
}

function handleCatchError(error) {
    throw error;
}

function getHeader(){
    if(apiToken) {
        return {
            ...headers,
            'Authorization': 'Bearer ' +apiToken,
        }
    }

    return headers;
}

export var get = function (path) {

    return Promise.resolve(getFetch()(path, {
        headers: getHeader(),
        credentials: 'same-origin'
    })).then(handleError).catch(handleCatchError);
}

export var deleteRequest = function (path, data) {
    return Promise.resolve(getFetch()(path, {
        method: "DELETE",
        headers: getHeader(),
        credentials: 'same-origin',
        body: stringifyContent(data)
    })).then(handleError).catch(handleCatchError)
}

export var put = function (path, data) {
    return Promise.resolve(getFetch()(path, {
        method: "PUT",
        headers: getHeader(),
        credentials: 'same-origin',
        body: stringifyContent(data)
    })).then(handleError).catch(handleCatchError)
}

export var post = function (path, data) {
    return Promise.resolve(getFetch()(path, {
        method: "POST",
        headers: getHeader(),
        credentials: 'same-origin',
        body: stringifyContent(data)
    })).then(handleError).catch(handleCatchError)
}

function stringifyContent(data) {
    if (!data) return null;
    if (typeof data == "string") {
        return data;
    }

    return JSON.stringify(data);
}