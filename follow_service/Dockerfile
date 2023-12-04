# Build stage
FROM ubuntu:20.04 as builder

# Avoid prompts from apt-get
ENV DEBIAN_FRONTEND=noninteractive

# Install Go and other build dependencies
RUN apt-get update && apt-get install -y \
    golang-go \
    librdkafka-dev \
    pkg-config \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Set up the working directory
WORKDIR /app

# Copy go mod and sum files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy the source code
COPY . .

# Build the Go app with CGO enabled
RUN CGO_ENABLED=1 GOOS=linux go build -a -o main .

# Runtime stage
FROM ubuntu:20.04

# Avoid prompts from apt-get
ENV DEBIAN_FRONTEND=noninteractive

# Install runtime dependencies
RUN apt-get update && apt-get install -y \
    librdkafka-dev \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Set up the working directory
WORKDIR /root/

# Copy the built binary from the builder image
COPY --from=builder /app/main .

# Expose the necessary port
EXPOSE 8089

# Command to run the executable
CMD ["./main"]
