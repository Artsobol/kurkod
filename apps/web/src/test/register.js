import axios from "axios";

async function register() {
  try {
    const response = await axios.post("http://localhost:8189/auth/register", {
      username: "sasxaxa",
      email: "alalalal383232sdsdd34243098asdsla@gmail.com",
      password: "Pass12345678.",
      confirmPassword: "Pass12345678."
    });

    console.log("✅ Регистрация прошла успешно:", response.data);
  } catch (error) {
    console.error("❌ Ошибка регистрации:", error);
  }
}

register();
