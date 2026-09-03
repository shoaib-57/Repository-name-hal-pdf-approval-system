import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
import Card from "../../components/ui/Card/Card";
import Input from "../../components/ui/Input/Input";
import Button from "../../components/ui/Button/Button";
import logo from "../../assets/logo/hal-logo.png";
import aircraft from "../../assets/images/aircraft.png";
import { login } from "../../services/authService";
function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");

  const [password, setPassword] = useState("");
  const handleLogin = async () => {
    try {
      const response = await login({
        email,

        password,
      });

      localStorage.setItem("token", response.token);

      localStorage.setItem("user", JSON.stringify(response));

      console.log(response);

      navigate("/dashboard");
      
    } catch (error) {
      console.error(error);
    }
  };
  return (
    <div className="login-page">
      {/* LEFT SIDE */}

      <section className="login-left">
        <div className="brand">
          <img src={logo} alt="HAL Logo" className="brand-logo" />

          <h2>Hindustan Aeronautics Limited</h2>

          <p className="brand-title">
            Document Approval &
            <br />
            Workflow Management System
          </p>
        </div>

        <div className="aircraft">
          <img src={aircraft} alt="Aircraft" className="aircraft-image" />
        </div>
      </section>

      {/* RIGHT SIDE */}

      <section className="login-right">
        <Card>
          <div className="login-card">
            <h2>Sign in to your account</h2>

            <p>Enter your credentials to access the system</p>

            <Input
              label="Email"
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <Input
              label="Password"
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <div className="login-options">
              <label className="remember-me">
                <input type="checkbox" />
                Remember me
              </label>

              <a href="#">Forgot password?</a>
            </div>

            <Button text="Sign In" onClick={handleLogin} />

            <div className="login-copyright">
              © 2026 HAL. All rights reserved.
            </div>
          </div>
        </Card>
      </section>
    </div>
  );
}

export default Login;
