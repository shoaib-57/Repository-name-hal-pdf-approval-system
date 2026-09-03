import { useEffect, useState } from "react";

import DashboardLayout from "../../layouts/DashboardLayout";
import DashboardHeader from "../../components/dashboard/DashboardHeader/DashboardHeader";
import DashboardSection from "../../components/dashboard/DashboardSection/DashboardSection";
import Table from "../../components/common/Table/Table";
import StatusBadge from "../../components/common/StatusBadge/StatusBadge";

import {
  getAllDocuments,
  downloadDocument,
  approveDocument,
  rejectDocument,
} from "../../services/documentService";

function Documents() {
  const [documents, setDocuments] = useState([]);

  useEffect(() => {
    loadDocuments();
  }, []);

  const loadDocuments = async () => {
    try {
      const response = await getAllDocuments();

      setDocuments(response);
    } catch (error) {
      console.error(error);
    }
  };

  const handleDownload = async (id, documentName) => {
    try {
      const response = await downloadDocument(id);

      const url = window.URL.createObjectURL(new Blob([response.data]));

      const link = document.createElement("a");

      link.href = url;

      link.setAttribute("download", documentName);

      document.body.appendChild(link);

      link.click();

      link.remove();
    } catch (error) {
      console.error(error);

      alert("Download failed");
    }
  };
  const handleApprove = async (documentId) => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));

      await approveDocument(documentId, user.id, "Approved");

      alert("Document Approved");

      loadDocuments();
    } catch (error) {
      console.error(error);

      alert("Approval Failed");
    }
  };

  const handleReject = async (documentId) => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));

      await rejectDocument(documentId, user.id, "Rejected");

      alert("Document Rejected");

      loadDocuments();
    } catch (error) {
      console.error(error);

      alert("Rejection Failed");
    }
  };

  const columns = [
    {
      key: "documentName",
      label: "Document",
    },

    {
      key: "uploadedBy",
      label: "Uploaded By",
    },

    {
      key: "status",
      label: "Status",
      render: (value) => <StatusBadge status={value} />,
    },

    {
      key: "createdAt",
      label: "Uploaded On",
    },

    {
      key: "action",
      label: "Action",
      render: (_, row) => {
        const user = JSON.parse(localStorage.getItem("user"));

        const canApprove =
          (user.role === "ADMIN" || user.role === "MANAGER") &&
          row.status === "PENDING";

        return (
          <div
            style={{
              display: "flex",
              gap: "8px",
            }}
          >
            <button
              className="download-btn"
              onClick={() => handleDownload(row.id, row.documentName)}
            >
              Download
            </button>

            {canApprove && (
              <>
                <button
                  className="approve-btn"
                  onClick={() => handleApprove(row.id)}
                >
                  Approve
                </button>

                <button
                  className="reject-btn"
                  onClick={() => handleReject(row.id)}
                >
                  Reject
                </button>
              </>
            )}
          </div>
        );
      },
    },
  ];

  return (
    <DashboardLayout>
      <DashboardHeader
        title="Documents"
        subtitle="Manage all uploaded documents"
      />

      <DashboardSection title="All Documents">
        <Table columns={columns} data={documents} />
      </DashboardSection>
    </DashboardLayout>
  );
}

export default Documents;
