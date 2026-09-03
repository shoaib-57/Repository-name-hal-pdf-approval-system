import api from "../api/axios";

export const uploadDocument = async (file, employeeId) => {

    const token = localStorage.getItem("token");

    const formData = new FormData();

    formData.append("file", file);

    formData.append("employeeId", employeeId);

    const response = await api.post(
        "/documents/upload",
        formData,
        {
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "multipart/form-data"
            }
        }
    );



    return response.data;

};

export const downloadDocument = async (id) => {

    const token = localStorage.getItem("token");

    const response = await api.get(
        `/documents/download/${id}`,
        {
            headers: {
                Authorization: `Bearer ${token}`
            },
            responseType: "blob"
        }
    );

    return response;
};

export const approveDocument = async (documentId, managerId, comments) => {

    const token = localStorage.getItem("token");

    const response = await api.patch(

        `/documents/${documentId}/approve`,

        {
            managerId,
            comments
        },

        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }

    );

    return response.data;
};

export const rejectDocument = async (documentId, managerId, comments) => {

    const token = localStorage.getItem("token");

    const response = await api.patch(

        `/documents/${documentId}/reject`,

        {
            managerId,
            comments
        },

        {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }

    );

    return response.data;
};

export const getAllDocuments = async () => {

    const token = localStorage.getItem("token");

    const response = await api.get("/documents", {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    return response.data;
};