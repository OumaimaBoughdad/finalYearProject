# Stage 1: Build the Angular application
FROM node:18 as build-stage

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the Angular application
RUN npm run build -- --prod

# Stage 2: Serve the application using Nginx
FROM nginx:alpine

# Copy the built Angular app from the build-stage
COPY --from=build-stage /app/dist/my-angular-app /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
