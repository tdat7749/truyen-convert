/** @type {import('tailwindcss').Config} */
module.exports = {
  prefix:"tw-",
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      // "primary":"#76c5f0",
      // "text-primary":"#105481",
      // "color-border":"#f0f0f0"
    },
  },
  plugins: [],
  corePlugins: { preflight: false }
}