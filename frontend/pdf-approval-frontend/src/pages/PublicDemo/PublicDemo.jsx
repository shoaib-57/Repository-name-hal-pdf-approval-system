import { useNavigate } from "react-router-dom";
import { FaFileAlt, FaCheckCircle, FaClock, FaTimesCircle, FaShieldAlt, FaQrcode, FaUserShield, FaArrowRight } from "react-icons/fa";
import logo from "../../assets/logo/hal-logo.png";
import "./PublicDemo.css";

function PublicDemo() {
  const navigate = useNavigate();

  const stats = [
    { label: "Documents", value: "13", icon: <FaFileAlt /> },
    { label: "Approved", value: "10", icon: <FaCheckCircle /> },
    { label: "Pending", value: "0", icon: <FaClock /> },
    { label: "Rejected", value: "3", icon: <FaTimesCircle /> },
  ];

  const features = [
    { icon: <FaUserShield />, title: "Role-Based Access", text: "Secure access for employees, managers and administrators using JWT authentication." },
    { icon: <FaFileAlt />, title: "Document Workflow", text: "Upload, review, approve and reject PDF documents through a structured workflow." },
    { icon: <FaQrcode />, title: "QR-Stamped PDF", text: "Approved documents receive a QR code on the final existing page together with approval information." },
    { icon: <FaShieldAlt />, title: "Secure Documents", text: "Document access is controlled by authenticated users and their roles." },
  ];

  return (
    <div className="public-demo-page">
      <header className="public-demo-header">
        <div className="public-brand">
          <img src={logo} alt="HAL Logo" />
          <div><strong>Hindustan Aeronautics Limited</strong><span>Document Approval &amp; Workflow Management System</span></div>
        </div>
        <button className="public-login-button" onClick={() => navigate("/login")}>Login</button>
      </header>

      <main>
        <section className="public-hero">
          <div className="hero-content">
            <span className="demo-label">LIVE PROJECT DEMO</span>
            <h1>Document Approval &amp; Workflow Management</h1>
            <p>A secure digital workflow for uploading, reviewing, approving and managing PDF documents.</p>
            <div className="hero-actions">
              <button className="primary-demo-button" onClick={() => navigate("/login")}>Try the Application <FaArrowRight /></button>
              <span className="no-login-note">Explore this overview without logging in</span>
            </div>
          </div>

          <div className="hero-panel">
            <div className="panel-title">Approval Workflow</div>
            <div className="workflow">
              <div className="workflow-step"><span>1</span><div><strong>Employee</strong><small>Upload PDF</small></div></div>
              <div className="workflow-line" />
              <div className="workflow-step"><span>2</span><div><strong>Manager / Admin</strong><small>Review document</small></div></div>
              <div className="workflow-line" />
              <div className="workflow-step"><span>3</span><div><strong>Decision</strong><small>Approve or reject</small></div></div>
              <div className="workflow-line" />
              <div className="workflow-step"><span>4</span><div><strong>Approved PDF</strong><small>QR + approval information</small></div></div>
            </div>
          </div>
        </section>

        <section className="public-stats-section">
          <div className="section-heading"><span>DEMO OVERVIEW</span><h2>System at a glance</h2><p className="demo-data-note">Illustrative portfolio data — login to use the working application.</p></div>
          <div className="public-stats-grid">
            {stats.map((stat) => <div className="public-stat-card" key={stat.label}><div className="public-stat-icon">{stat.icon}</div><div><strong>{stat.value}</strong><span>{stat.label}</span></div></div>)}
          </div>
        </section>

        <section className="features-section">
          <div className="section-heading"><span>KEY FEATURES</span><h2>Built around a real approval workflow</h2></div>
          <div className="features-grid">
            {features.map((feature) => <article className="feature-card" key={feature.title}><div className="feature-icon">{feature.icon}</div><h3>{feature.title}</h3><p>{feature.text}</p></article>)}
          </div>
        </section>

        <section className="technology-section">
          <div><span>TECHNOLOGY STACK</span><h2>Modern backend and frontend architecture</h2></div>
          <div className="technology-list">
            {["Java 21", "Spring Boot", "Spring Security", "JWT", "PostgreSQL", "JPA / Hibernate", "PDFBox", "ZXing", "React", "Vite", "Docker"].map((technology) => <span key={technology}>{technology}</span>)}
          </div>
        </section>

        <section className="login-cta">
          <div><span>WANT TO TRY THE ACTUAL APPLICATION?</span><h2>Sign in to access the working dashboard.</h2></div>
          <button onClick={() => navigate("/login")}>Open Login <FaArrowRight /></button>
        </section>
      </main>
      <footer className="public-footer">© 2026 HAL Document Approval System · Portfolio Demo</footer>
    </div>
  );
}

export default PublicDemo;
