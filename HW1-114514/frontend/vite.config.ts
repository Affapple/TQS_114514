import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";

const __dirname = path.dirname("./src");

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@assets": path.resolve(__dirname, "src/assets"),
      "@components": path.resolve(__dirname, "src/components"),
      "@pages": path.resolve(__dirname, "src/pages"),
      "@api": path.resolve(__dirname, "src/api"),
      "@Types": path.resolve(__dirname, "src/Types"),
      "@hooks": path.resolve(__dirname, "src/hooks"),
    },
  },
});
