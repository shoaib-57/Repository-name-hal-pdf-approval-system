import DashboardLayout from "../../layouts/DashboardLayout";
import "./Upload.css";
import { useState } from "react";
import { uploadDocument } from "../../services/documentService";
import DashboardHeader from "../../components/dashboard/DashboardHeader/DashboardHeader";
function Upload() {
  const [file, setFile] = useState(null);
  const handleUpload = async () => {
    if (!file) {
      alert("Please select a PDF file");

      return;
    }

    try {
      const user = JSON.parse(localStorage.getItem("user"));

      await uploadDocument(file, user.id);

      alert("Document uploaded successfully!");
    } catch (error) {
      console.error(error);

      alert("Upload failed");
    }
  };
  return (
    <DashboardLayout>
      <>
        <DashboardHeader
          title="Upload Document"
          subtitle="Upload a new PDF document"
        />

        <div className="upload-card">
          <div className="upload-row">
            <div className="form-group">
              <label>Document Title</label>
              <input type="text" placeholder="Enter document title" />
            </div>

            <div className="form-group">
              <label>Category</label>
              <select>
                <option>Select Category</option>
              </select>
            </div>

            <div className="form-group">
              <label>Department</label>
              <select>
                <option>Select Department</option>
              </select>
            </div>
          </div>

          <div className="form-group">
            <label>Description</label>

            <textarea rows="5" placeholder="Enter description"></textarea>
          </div>

          <div className="upload-row">
            <div className="form-group">
              <label>Choose PDF</label>

              <input
                type="file"
                accept=".pdf"
                onChange={(e) => setFile(e.target.files[0])}
              />
            </div>

            <div className="preview-box">PDF Preview</div>
          </div>

          <div className="button-group">
            <button className="cancel-btn">Cancel</button>

            <button className="upload-btn" onClick={handleUpload}>
              Upload Document
            </button>
          </div>
        </div>
      </>
    </DashboardLayout>
  );
}

export default Upload;
