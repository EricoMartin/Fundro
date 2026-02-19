# 🚀 Fundro - Secure Group Payment Platform

<div align="center">

![Fundro Logo](https://via.placeholder.com/150x150/2563EB/FFFFFF?text=Fundro)

**Trustworthy Group Contributions Made Simple**

[![Android](https://img.shields.io/badge/Android-10%2B-3DDC84?logo=android&logoColor=white)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

[Download APK](#-download--installation) • [Features](#-key-features) • [Documentation](#-documentation) • [Demo](#-demo)

</div>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Problem Statement](#-problem-statement)
- [Key Features](#-key-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Download & Installation](#-download--installation)
- [User Guide](#-user-guide)
- [API Documentation](#-api-documentation)
- [Development Setup](#-development-setup)
- [Backend Deployment](#-backend-deployment)
- [Screenshots](#-screenshots)
- [Security Features](#-security-features)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

## 🌟 Overview

**Fundro** is a secure, mobile-first platform designed to revolutionize group contributions in Nigeria. Built on an escrow-based system, Fundro ensures that pooled funds are only released when all members have contributed, eliminating the trust issues common in informal group savings schemes.

### Why Fundro?

In Nigeria's vibrant social economy, friends, families, and colleagues frequently pool money for shared goals—weddings, birthdays, emergencies, or group investments. However, these arrangements often rely on informal trust, leading to:

- **Abandoned Contributions**: Some members contribute, others don't
- **Mismanaged Funds**: No transparent tracking of who paid what
- **Dispute Resolution**: No clear audit trail or accountability
- **Security Concerns**: Cash handling and informal record-keeping risks

**Fundro solves this by acting as a neutral, secure intermediary.**

---

## 🎯 Problem Statement

Traditional group contribution systems in Nigeria face critical challenges:

| Problem | Impact | Fundro Solution |
|---------|--------|-----------------|
| **Lack of Trust** | Members hesitate to contribute first | Escrow holds funds until target is met |
| **No Transparency** | Unclear who has paid | Real-time contribution tracking |
| **Manual Tracking** | Spreadsheets, WhatsApp messages | Automated, digital ledger |
| **Payment Friction** | Cash collection, bank transfers | Integrated Paystack payments |
| **No Accountability** | Disputes with no proof | Immutable transaction records |

---

## ✨ Key Features

### 🔒 Core Features

#### 1. **Escrow-Based Contributions**
- Funds held securely until group reaches target amount
- Automatic release when fully funded
- Refund protection if target isn't met

#### 2. **Real-Time Payment Tracking**
- Live progress bars showing contribution status
- Instant notifications on member payments
- Transparent ledger of all transactions

#### 3. **Secure Authentication**
- JWT-based authentication
- Encrypted credential storage
- Biometric support (planned)

#### 4. **Group Management**
- Create public or private groups
- Set contribution deadlines
- Invite members via username search
- Generate shareable join codes

#### 5. **Integrated Payments**
- Paystack gateway integration
- Support for cards, bank transfers, USSD
- Webhook-based verification
- PCI DSS compliant

#### 6. **Push Notifications**
- Firebase Cloud Messaging
- Real-time payment alerts
- Group milestone notifications
- Invitation reminders

#### 7. **KYC Verification** (Planned)
- BVN verification for recipients
- Bank account linking
- Identity verification for large amounts

### 🎨 User Experience Features

- **Material 3 Design**: Modern, intuitive interface
- **Dark Mode Ready**: Adaptive theming
- **Smooth Animations**: Polished interactions
- **Offline Support** (Planned): View cached data
- **Multi-language** (Planned): English, Yoruba, Hausa, Igbo

---

## 🛠️ Technology Stack

### Android Client
- **Language**: Kotlin 1.9.22
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt (Dagger)
- **Networking**: Retrofit 2 + OkHttp
- **Image Loading**: Coil
- **Database**: Room (Planned)
- **Security**: EncryptedSharedPreferences
- **Payments**: Paystack Android SDK
- **Notifications**: Firebase Cloud Messaging


---

## 📥 Download & Installation

### Prerequisites

- Android device running **Android 10 (API 29)** or higher
- Active internet connection
- Valid email address and phone number

### Option 1: Download APK (Recommended)

1. **Download the latest release:**
    - Visit: [Releases Page](https://github.com/yourusername/fundro/releases)
    - Download: `fundro-v1.0.0.apk`

2. **Enable Unknown Sources** (if needed):
    - Go to: `Settings → Security → Unknown Sources`
    - Enable installation from unknown sources

3. **Install the APK:**
    - Open the downloaded file
    - Tap "Install"
    - Wait for installation to complete
    - Tap "Open"

### Option 2: Build from Source

```bash
# Clone the repository
git clone https://github.com/yourusername/fundro-android.git
cd fundro-android

# Open in Android Studio
# File → Open → Select fundro-android folder

# Update base URL in NetworkModule.kt
# Find: BASE_URL
# Replace with: Your deployed backend URL

# Build and run
# Click "Run" button or press Shift+F10
```
### ⚠️ Important: Backend Configuration
Fundro requires a running backend server to function. The app does not currently support offline mode.

### Backend URL Configuration:

The app is pre-configured to connect to our hosted backend:

If you're running your own backend, update the URL in:
- File: `app/src/main/java/com/basebox/fundro/core/network/NetworkModule.kt`
- Line: `private const val BASE_URL = "YOUR_BACKEND_URL/api"`

---

## 📱 User Guide

### Getting Started

#### 1. **First Launch - Onboarding**

<div align="center">

| Step 1 | Step 2 | Step 3 |
|--------|--------|--------|
| ![Onboarding 1](https://via.placeholder.com/250x500/2563EB/FFFFFF?text=Welcome) | ![Onboarding 2](https://via.placeholder.com/250x500/2563EB/FFFFFF?text=How+It+Works) | ![Onboarding 3](https://via.placeholder.com/250x500/2563EB/FFFFFF?text=Get+Started) |

</div>

- Swipe through to learn about Fundro
- Tap "Get Started" to begin

#### 2. **Create Account**

**Required Information:**
- Username (3-50 characters, letters/numbers/underscore)
- Full Name (your legal name)
- Email Address (valid email format)
- Phone Number (Nigerian format: +234...)
- Password (min 8 chars, 1 uppercase, 1 number, 1 special char)

**Steps:**
1. Tap "Sign Up"
2. Fill in your details
3. Agree to Terms & Conditions
4. Tap "Create Account"
5. Account created automatically—redirects to Login

#### 3. **Login**

**Credentials:**
- Username or Email
- Password

**Features:**
- Remember Me (keeps you logged in)
- Forgot Password (planned)

---

### Creating Your First Group

#### Step 1: Basic Information

1. Tap the **"+" Floating Action Button** on home screen
2. Enter group details:
    - **Name**: e.g., "Ade's 30th Birthday Gift"
    - **Description**: Optional details about the group
    - **Target Amount**: ₦5,000 - ₦10,000,000
    - **Category**: Gift, Event, Subscription, Campaign, or General

3. Tap **"Continue"**

#### Step 2: Advanced Settings

1. **Deadline** (Optional): Set a funding deadline
2. **Visibility**:
    - **Private**: Only invited members can see
    - **Public**: Anyone can discover and join
    - **Unlisted**: Anyone with the link can join
3. **Max Members** (Optional): Limit group size
4. **Generate Join Code**: Create a shareable code

5. Tap **"Create Pool"**

**Result**: Group is created! You're taken to the group detail page.

---

### Inviting Members

#### Method 1: Search by Username

1. Open your group
2. Tap **"Add Members"**
3. Type username in search bar (e.g., "janedoe")
4. Results appear as you type
5. Tap user to select (chip appears at top)
6. Select multiple users
7. *Optional*: Enter expected amount per member
8. Tap **"Add X Members"**

#### Method 2: Share Join Code (Planned)

1. Open group details
2. Tap "Share" icon
3. Copy join code or share link
4. Members enter code in app to join

---

### Making a Contribution

#### Payment Flow

1. **Open Group**: Navigate to the group
2. **Tap Contribute**: Click the "Contribute" button
3. **Enter Amount**:
    - Minimum: ₦100
    - Maximum: Group's remaining target
    - Suggested amounts available
4. **Review Summary**: Check details
5. **Pay via Paystack**:
    - Enter card details OR
    - Use saved card
    - Complete 3D Secure (PIN/OTP)
6. **Verification**:
    - App checks payment status
    - Shows success/failure
7. **Confirmation**:
    - Group amounts update
    - Progress bar refreshes
    - Notification sent to owner

#### Test Cards (For Testing Only)

**Successful Payment:**
```
Card: 4084 0840 8408 4081
CVV: 408
Expiry: 12/25
PIN: 0000
OTP: 123456
```

**Failed Payment:**
```
Card: 5060 6666 6666 6666 666
```

---

### Managing Your Profile

#### View Profile

1. Tap your avatar (top-right)
2. View your information:
    - Full name, username, email
    - Phone number
    - Member since date
    - KYC status

#### Edit Profile

1. Tap "Edit" icon
2. Update:
    - Full Name
    - Username (unique)
    - Phone Number
3. Tap "Save Changes"

#### Logout

1. Tap "Log Out" in profile
2. Confirm logout
3. Redirected to login screen

---

### Viewing Notifications

1. Tap bell icon (top-right of home)
2. See all notifications:
    - Payment confirmations
    - Member invitations
    - Group funded alerts
    - KYC updates
3. Tap notification to view details
4. Mark all as read

---

## 🔌 API Documentation

### Base URL


Local Dev:  http://localhost:8080/api
### Authentication
All authenticated endpoints require JWT token in header:
httpAuthorization: Bearer <your_jwt_token>
Key Endpoints
Authentication
Register User
httpPOST /auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "fullName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "+2348012345678",
  "password": "Test1234!"
}

Response: 201 Created
{
  "id": "user-uuid",
  "username": "johndoe",
  "email": "john@example.com",
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
Login
httpPOST /auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "Test1234!"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "user": { ... }
}
Groups
Create Group
httpPOST /groups
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Weekend Trip",
  "description": "Lagos getaway",
  "targetAmount": 50000.00,
  "visibility": "PRIVATE",
  "category": "EVENT",
  "deadline": "2024-12-31T23:59:59"
}

Response: 201 Created
{
  "id": "group-uuid",
  "name": "Weekend Trip",
  "status": "OPEN",
  ...
}
Get My Groups
httpGET /groups/my-groups?page=0&size=20
Authorization: Bearer <token>

Response: 200 OK
{
  "groups": [ ... ],
  "totalElements": 5,
  "totalPages": 1
}
Contributions
Initiate Payment
httpPOST /contributions/initiate
Authorization: Bearer <token>
Content-Type: application/json

{
  "groupId": "group-uuid",
  "amount": 10000.00
}

Response: 200 OK
{
  "contributionId": "contrib-uuid",
  "authorizationUrl": "https://checkout.paystack.com/...",
  "accessCode": "xxxxxxxxxx",
  "reference": "contrib_ref_123"
}
Verify Payment
httpGET /contributions/{contributionId}/verify
Authorization: Bearer <token>

Response: 200 OK
{
  "contributionId": "contrib-uuid",
  "status": "COMPLETED",
  "amount": 10000.00,
  "paidAt": "2024-02-18T10:30:00"
}
```

```
### Full API Documentation
Complete API documentation with all endpoints, request/response schemas, and error codes:
📄 View Full API Docs

## 🚀 Development Setup
#     Android Setup
  # Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17
- Android SDK 34
- Gradle 8.2

## Steps

**Clone Repository**

bash git clone https://github.com/yourusername/fundro-android.git
cd fundro-android


2. **Open in Android Studio**

File → Open → Select fundro-android folder

**Configure API Keys**

- Create local.properties:
- properties PAYSTACK_PUBLIC_KEY=pk_test_xxxxxxxxxxxxx
- BASE_URL=http://10.0.2.2:8080/api


4. **Sync Gradle**
File → Sync Project with Gradle Files


5. **Run App**

Run → Run 'app' or Shift+F10
### Backend Setup
**Prerequisites**

- Java 17 or later
- Maven 3.8+
- PostgreSQL 15+
- Paystack account (test mode)

## Steps

Clone Repository

bash git clone https://github.com/yourusername/fundro-backend.git
cd fundro-backend

## Configure Database

# Edit src/main/resources/application.properties:
propertiesspring.datasource.url=jdbc:postgresql://localhost:5432/fundro
spring.datasource.username=postgres
spring.datasource.password=your_password

paystack.secret.key=sk_test_xxxxxxxxxxxxx
paystack.public.key=pk_test_xxxxxxxxxxxxx

jwt.secret=your-256-bit-secret
jwt.expiration=86400000

# Create Database

bash psql -U postgres
CREATE DATABASE fundro;
\q

# Run Application

bash./mvnw spring-boot:run

# Verify

bash curl http://localhost:8080/actuator/health
Database Schema
Run migrations:
bash./mvnw flyway:migrate
Or use provided SQL scripts in src/main/resources/db/migration/

### ☁️ Backend Deployment
# Option 1: Railway (Recommended)

Create Account: https://railway.app

New Project: Click "New Project"
Deploy from GitHub: Connect your repository
Add PostgreSQL: Add database service

# Environment Variables:

env SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}
PAYSTACK_SECRET_KEY=sk_test_xxxxx
PAYSTACK_PUBLIC_KEY=pk_test_xxxxx
JWT_SECRET=your-secret-key

Deploy: Railway auto-deploys on push
Get URL: Copy your deployment URL

# Option 2: Render

Create Account: https://render.com
New Web Service: From GitHub
Configure:

Build Command: ./mvnw clean package -DskipTests
Start Command: java -jar target/fundro-backend-0.0.1-SNAPSHOT.jar


Add Database: Create PostgreSQL instance
Environment Variables: Add all secrets
Deploy: Manual or auto-deploy

# Option 3: Heroku
bash# Login
heroku login

# Create app
heroku create fundro-api

# Add PostgreSQL
heroku addons:create heroku-postgresql:mini

# Set environment variables
heroku config:set PAYSTACK_SECRET_KEY=sk_test_xxxxx
heroku config:set JWT_SECRET=your-secret-key

# Deploy
git push heroku main

# Open
heroku open
Post-Deployment

# Update Android App:

Change BASE_URL in NetworkModule.kt
Rebuild APK


# Configure Paystack Webhook:

Dashboard → Settings → Webhooks
URL: https://your-backend.railway.app/api/webhooks/paystack


# Test:

bash curl https://your-backend.railway.app/actuator/health

```