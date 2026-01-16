FROM nginx:alpine
LABEL authors="Adias"

# Hapus config default nginx
RUN rm /etc/nginx/conf.d/default.conf

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy file HTML, JS, CSS ke nginx
COPY . /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# docker build -t adiasafnan/web-spring-oauth2:latest .
