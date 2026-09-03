import { useNavigate } from "react-router-dom";
import { getAllDocuments } from "../../services/documentService";
import DashboardSection from "../../components/dashboard/DashboardSection/DashboardSection";
import Table from "../../components/common/Table/Table";
import StatusBadge from "../../components/common/StatusBadge/StatusBadge";
// import { dashboardColumns, dashboardData } from "../../data/dashboardData";
import DashboardLayout from "../../layouts/DashboardLayout";
import DashboardHeader from "../../components/dashboard/DashboardHeader/DashboardHeader";
import StatsCard from "../../components/dashboard/StatsCard/StatsCard";
import "./Dashboard.css";
import { FaEye } from "react-icons/fa";
import { useEffect, useState } from "react";
import { getDashboardStats } from "../../services/dashboardService";
import {
  FaFileAlt,
  FaClock,
  FaCheckCircle,
  FaTimesCircle,
} from "react-icons/fa";

function Dashboard() {
  const [stats, setStats] = useState({
    totalDocuments: 0,

    pendingDocuments: 0,

    approvedDocuments: 0,

    rejectedDocuments: 0,
  });

  const [recentDocuments, setRecentDocuments] = useState([]);

  useEffect(() => {
    const fetchDashboardStats = async () => {
      try {
        const response = await getDashboardStats();

        setStats(response);
      } catch (error) {
        console.error("Failed to load dashboard stats", error);
      }
    };

    fetchDashboardStats();
    loadRecentDocuments();
  }, []);
  const navigate = useNavigate();
  const loadRecentDocuments = async () => {
    try {
      const response = await getAllDocuments();

      setRecentDocuments(response.slice(0, 5));
    } catch (error) {
      console.error(error);
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
      render: (_, row) => (
        <FaEye
          style={{
            color: "#2563eb",
            cursor: "pointer",
            fontSize: "18px",
          }}
          onClick={() => navigate("/documents")}
        />
      ),
    },
  ];
  return (
    <DashboardLayout>
      <DashboardHeader
        title="Dashboard"
        subtitle="Overview of document workflow"
      />
      <div className="stats-grid">
        <StatsCard
          title="Total Documents"
          value={stats.totalDocuments}
          icon={<FaFileAlt />}
          color="blue"
        />

        <StatsCard
          title="Pending Approval"
          value={stats.pendingDocuments}
          icon={<FaClock />}
          color="orange"
        />

        <StatsCard
          title="Approved"
          value={stats.approvedDocuments}
          icon={<FaCheckCircle />}
          color="green"
        />

        <StatsCard
          title="Rejected"
          value={stats.rejectedDocuments}
          icon={<FaTimesCircle />}
          color="red"
        />
      </div>
      <DashboardSection title="Recent Documents" actionText="View All">
        <Table columns={columns} data={recentDocuments} />
      </DashboardSection>
    </DashboardLayout>
  );
}

export default Dashboard;
