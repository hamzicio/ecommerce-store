FROM node:16.19.0-alpine

WORKDIR /app
EXPOSE 4200

COPY . ./

RUN npm install -g @angular/cli
RUN npm install

CMD ["ng", "serve","--host", "0.0.0.0"]
