import api from "../api/axios";

export const login = async (loginData) => {

    const response = await api.post("/auth/login", loginData);

    return response.data;

};