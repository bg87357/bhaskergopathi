// Login functionality
document.addEventListener('DOMContentLoaded', function() {
    // Check if already logged in
    const token = localStorage.getItem('authToken');
    if (token) {
        window.location.href = '/';
        return;
    }

    // Initialize login form
    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', handleLogin);

    // Set today's date as default
    const today = new Date().toISOString().split('T')[0];
    
    // Demo credential buttons
    addDemoCredentialHandlers();
});

// Handle login form submission
async function handleLogin(e) {
    e.preventDefault();
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const loginBtn = document.querySelector('.login-btn');
    const errorMessage = document.getElementById('errorMessage');
    
    // Show loading state
    loginBtn.disabled = true;
    loginBtn.innerHTML = '<div class="loading"></div> Signing In...';
    errorMessage.style.display = 'none';
    
    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });
        
        if (response.ok) {
            const data = await response.json();
            
            // Store authentication data
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('currentUser', JSON.stringify({
                email: data.email,
                role: data.role,
                staffId: data.staffId,
                staffName: data.staffName,
                clinicId: data.clinicId
            }));
            
            // Redirect to main application
            window.location.href = '/';
        } else {
            const errorData = await response.json();
            showError(errorData.message || 'Invalid email or password');
        }
    } catch (error) {
        console.error('Login error:', error);
        showError('Connection error. Please try again.');
    } finally {
        // Reset button state
        loginBtn.disabled = false;
        loginBtn.innerHTML = '<i class="fas fa-sign-in-alt"></i> Sign In';
    }
}

// Show error message
function showError(message) {
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.textContent = message;
    errorMessage.style.display = 'block';
}

// Toggle password visibility
function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.getElementById('passwordToggleIcon');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.classList.remove('fa-eye');
        toggleIcon.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        toggleIcon.classList.remove('fa-eye-slash');
        toggleIcon.classList.add('fa-eye');
    }
}

// Add demo credential handlers
function addDemoCredentialHandlers() {
    const demoUsers = document.querySelectorAll('.demo-user');
    
    demoUsers.forEach(user => {
        user.style.cursor = 'pointer';
        user.addEventListener('click', function() {
            const text = this.textContent;
            const emailMatch = text.match(/(\S+@\S+\.\S+)/);
            const passwordMatch = text.match(/\/\s*(\S+)$/);
            
            if (emailMatch && passwordMatch) {
                document.getElementById('email').value = emailMatch[1];
                document.getElementById('password').value = passwordMatch[1];
                
                // Add visual feedback
                this.style.background = '#e3f2fd';
                setTimeout(() => {
                    this.style.background = 'white';
                }, 200);
            }
        });
    });
}

// Handle remember me functionality
document.getElementById('loginForm').addEventListener('submit', function() {
    const rememberMe = document.getElementById('rememberMe').checked;
    if (rememberMe) {
        localStorage.setItem('rememberMe', 'true');
    }
});

// Load remembered credentials
window.addEventListener('load', function() {
    const rememberMe = localStorage.getItem('rememberMe');
    if (rememberMe) {
        document.getElementById('rememberMe').checked = true;
    }
});