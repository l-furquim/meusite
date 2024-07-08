import axios from "axios"


export const frontEndApi = axios.create({
    baseURL: "http://localhost:3000/api",
    headers: {
        "Content-Type" : "application/json"
    }
});

export const backendApi = axios.create({
    baseURL: "http://localhost:8080",
    headers : {
        "Content-Type" : "application/json"
    }
    });