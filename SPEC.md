# Medicine Reminder Web Application - Specification

## Project Overview
- **Project Name**: Medicine Reminder
- **Type**: Full-Stack Web Application
- **Core Functionality**: A comprehensive medicine reminder system that helps users track their medications, receive email notifications, and manage prescriptions
- **Target Users**: General public needing medication reminders, Healthcare administrators

## Technology Stack
- **Frontend**: HTML, CSS, JavaScript (Vanilla)
- **Backend**: Java, Spring Boot 3.x
- **Build Tool**: Maven
- **Database**: H2 (In-memory)
- **Authentication**: Spring Security (Session-Based, NO JWT)
- **Email**: Spring Boot Mail Sender

## UI/UX Specification

### Layout Structure
- **Sidebar Layout**: Fixed left sidebar (250px) for dashboard pages
- **Header**: Top bar with user info and logout
- **Main Content**: Fluid content area with cards
- **Responsive Breakpoints**:
  - Mobile: < 768px (sidebar becomes hamburger menu)
  - Tablet: 768px - 1024px
  - Desktop: > 1024px

### Visual Design

#### Color Palette
- **Primary**: #0ea5e9 (Medical Blue)
- **Primary Light**: #38bdf8
- **Primary Dark**: #0284c7
- **Secondary**: #10b981 (Success Green)
- **Accent**: #f59e0b (Warning Amber)
- **Danger**: #ef4444 (Red)
- **Background**: #f8fafc (Light Gray-Blue)
- **Card Background**: #ffffff
- **Text Primary**: #1e293b
- **Text Secondary**: #64748b
- **Sidebar Background**: #0f172a (Dark Navy)
- **Sidebar Text**: #e2e8f0

#### Typography
- **Font Family**: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif
- **Headings**: 
  - H1: 2rem (32px), font-weight: 700
  - H2: 1.5rem (24px), font-weight: 600
  - H3: 1.25rem (20px), font-weight: 600
- **Body**: 1rem (16px), font-weight: 400
- **Small**: 0.875rem (14px)

#### Spacing System
- **Base Unit**: 4px
- **Spacing Scale**: 4, 8, 12, 16, 24, 32, 48, 64px
- **Card Padding**: 24px
- **Section Margin**: 32px

#### Visual Effects
- **Card Shadow**: 0 1px 3px rgba(0,0,0,0.1), 0 1px 2px rgba(0,0,0,0.06)
- **Hover Shadow**: 0 10px 15px -3px rgba(0,0,0,0.1)
- **Border Radius**: 12px (cards), 8px (buttons), 6px (inputs)
- **Transitions**: all 0.3s cubic-bezier(0.4, 0, 0.2, 1)

### Components

#### Cards
- White background
- 12px border-radius
- Subtle shadow
- Hover: elevated shadow + slight scale (1.01)

#### Buttons
- Primary: Blue background, white text
- Secondary: White background, blue border
- Danger: Red background
- Disabled: Gray, 50% opacity

#### Forms
- Floating labels or top labels
- 12px border-radius
- Focus: Blue border glow
- Error: Red border + error message

#### Tables
- Striped rows (alternating #f8fafc)
- Hover highlight
- Sortable headers

#### Sidebar Navigation
- Dark navy background
- Icon + text menu items
- Active state: Blue highlight bar
- Hover: Subtle background change

## Functionality Specification

### Authentication Module
1. **Login**
   - Email + Password form
   - Remember me option
   - Session-based authentication
   - Redirect to dashboard on success

2. **Registration**
   - Name, Email, Password fields
   - Email validation
   - Password strength indicator
   - Auto-login after registration

3. **Session Management**
   - Timeout after 30 minutes inactivity
   - Concurrent session support
   - Logout clears session

4. **Role-Based Access**
   - ROLE_USER: Access own dashboard, medicines, prescriptions
   - ROLE_ADMIN: Access admin panel, all users data

### User Module

#### Dashboard
- Welcome message with user name
- Today's medicines card (count)
- Active reminders card (count)
- Quick action buttons
- Recent activity list

#### Medicine Reminder Management
1. **Add Reminder**
   - Medicine name (required)
   - Dosage (required)
   - Frequency: Daily, Weekly, Custom
   - Multiple times per day
   - Start date
   - End date (optional)
   - Notes

2. **Edit Reminder**
   - All fields editable
   - Update confirmation

3. **Delete Reminder**
   - Soft delete
   - Confirmation dialog

4. **View Today's Medicines**
   - Filter by today's date
   - Mark as Taken
   - Mark as Missed
   - Time-based sorting

5. **Reminder Status Tracking**
   - TAKEN: User confirmed
   - MISSED: Time passed without confirmation
   - History view with date range

### Prescription Module
1. **Upload Prescription**
   - PDF and Image support (jpg, png)
   - Max file size: 10MB
   - Store metadata in database

2. **View Prescriptions**
   - Grid/List view
   - Thumbnail preview
   - Full-size modal viewer

3. **Delete Prescription**
   - Confirmation required
   - Removes file and database record

### Admin Module

#### Dashboard
- Total users count
- Active reminders count
- Today's taken count
- Today's missed count

#### User Management
- View all users table
- Search by name/email
- Filter by status (active/blocked)
- Block/Unblock user
- Delete user

#### Data View
- View all reminders
- View all prescriptions
- Export capability (optional)

### Email Notification System
1. **Registration Email**
   - Welcome message
   - App features overview

2. **Reminder Email**
   - Medicine name
   - Dosage
   - Time
   - Action links

3. **Missed Medicine Email**
   - Alert message
   - Medicine details
   - Importance of compliance

## Database Schema

### User Entity
```
- id: Long (PK)
- name: String (100)
- email: String (unique, 150)
- password: String (encoded)
- role: String (ROLE_USER, ROLE_ADMIN)
- enabled: Boolean
- createdAt: LocalDateTime
```

### Medicine Entity
```
- id: Long (PK)
- name: String (200)
- dosage: String (100)
- frequency: Enum (DAILY, WEEKLY, CUSTOM)
- reminderTimes: String (JSON array)
- startDate: LocalDate
- endDate: LocalDate (nullable)
- notes: String (nullable)
- user: User (FK)
- createdAt: LocalDateTime
- active: Boolean
```

### ReminderStatus Entity
```
- id: Long (PK)
- medicine: Medicine (FK)
- status: Enum (TAKEN, MISSED)
- scheduledTime: LocalDateTime
- actionTime: LocalDateTime (nullable)
- user: User (FK)
```

### Prescription Entity
```
- id: Long (PK)
- fileName: String
- originalFileName: String
- fileType: String
- fileSize: Long
- uploadDate: LocalDateTime
- user: User (FK)
```

## API Endpoints

### Authentication
- GET /login - Login page
- POST /login - Process login
- GET /register - Registration page
- POST /register - Process registration
- GET /logout - Logout

### User
- GET /dashboard - User dashboard
- GET /medicines - List medicines
- GET /medicines/new - Add medicine form
- POST /medicines - Create medicine
- GET /medicines/{id}/edit - Edit form
- PUT /medicines/{id} - Update medicine
- DELETE /medicines/{id} - Delete medicine
- POST /medicines/{id}/take - Mark as taken
- POST /medicines/{id}/miss - Mark as missed

### Prescriptions
- GET /prescriptions - List prescriptions
- POST /prescriptions/upload - Upload prescription
- GET /prescriptions/{id}/download - Download file
- DELETE /prescriptions/{id} - Delete prescription

### Admin
- GET /admin - Admin dashboard
- GET /admin/users - User list
- POST /admin/users/{id}/block - Block user
- POST /admin/users/{id}/unblock - Unblock user
- DELETE /admin/users/{id} - Delete user
- GET /admin/medicines - All medicines
- GET /admin/prescriptions - All prescriptions

## Acceptance Criteria

### Authentication
- [ ] User can register with name, email, password
- [ ] User can login with email and password
- [ ] Invalid credentials show error message
- [ ] Logout redirects to login page
- [ ] Session persists across page refreshes
- [ ] Admin cannot access user dashboard

### User Dashboard
- [ ] Shows today's medicine count
- [ ] Shows active reminder count
- [ ] Displays recent activity
- [ ] Quick action buttons work

### Medicine Management
- [ ] Can add new medicine with all fields
- [ ] Can edit existing medicine
- [ ] Can delete medicine with confirmation
- [ ] Today's medicines display correctly
- [ ] Can mark medicine as taken
- [ ] Can mark medicine as missed

### Prescriptions
- [ ] Can upload PDF/Image
- [ ] Files display in grid
- [ ] Can view full image
- [ ] Can delete with confirmation

### Admin
- [ ] Dashboard shows statistics
- [ ] Can view all users
- [ ] Can search users
- [ ] Can block/unblock users
- [ ] Can view all medicines
- [ ] Can view all prescriptions

### Email
- [ ] Welcome email on registration
- [ ] Reminder email at scheduled time
- [ ] Missed medicine alert

### UI/UX
- [ ] All pages responsive
- [ ] Smooth transitions
- [ ] Loading indicators
- [ ] Toast notifications
- [ ] Consistent styling
