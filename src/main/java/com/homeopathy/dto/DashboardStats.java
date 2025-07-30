package com.homeopathy.dto;

public class DashboardStats {
    private long totalPatients;
    private long totalAppointments;
    private long todayAppointments;
    private long scheduledAppointments;
    private long completedAppointments;
    private long cancelledAppointments;

    // Constructors
    public DashboardStats() {}

    // Getters and Setters
    public long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }

    public long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(long totalAppointments) { this.totalAppointments = totalAppointments; }

    public long getTodayAppointments() { return todayAppointments; }
    public void setTodayAppointments(long todayAppointments) { this.todayAppointments = todayAppointments; }

    public long getScheduledAppointments() { return scheduledAppointments; }
    public void setScheduledAppointments(long scheduledAppointments) { this.scheduledAppointments = scheduledAppointments; }

    public long getCompletedAppointments() { return completedAppointments; }
    public void setCompletedAppointments(long completedAppointments) { this.completedAppointments = completedAppointments; }

    public long getCancelledAppointments() { return cancelledAppointments; }
    public void setCancelledAppointments(long cancelledAppointments) { this.cancelledAppointments = cancelledAppointments; }
}