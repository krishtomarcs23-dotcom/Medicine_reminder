package com.medicinereminder.dto;

public class DashboardDTO {
    private long totalUsers;
    private long activeReminders;
    private long todayTaken;
    private long todayMissed;
    private long totalMedicines;
    private long totalPrescriptions;
    
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getActiveReminders() { return activeReminders; }
    public void setActiveReminders(long activeReminders) { this.activeReminders = activeReminders; }
    public long getTodayTaken() { return todayTaken; }
    public void setTodayTaken(long todayTaken) { this.todayTaken = todayTaken; }
    public long getTodayMissed() { return todayMissed; }
    public void setTodayMissed(long todayMissed) { this.todayMissed = todayMissed; }
    public long getTotalMedicines() { return totalMedicines; }
    public void setTotalMedicines(long totalMedicines) { this.totalMedicines = totalMedicines; }
    public long getTotalPrescriptions() { return totalPrescriptions; }
    public void setTotalPrescriptions(long totalPrescriptions) { this.totalPrescriptions = totalPrescriptions; }
    
    public static DashboardDTOBuilder builder() {
        return new DashboardDTOBuilder();
    }
    
    public static class DashboardDTOBuilder {
        private DashboardDTO dto = new DashboardDTO();
        public DashboardDTOBuilder totalUsers(long totalUsers) { dto.totalUsers = totalUsers; return this; }
        public DashboardDTOBuilder activeReminders(long activeReminders) { dto.activeReminders = activeReminders; return this; }
        public DashboardDTOBuilder todayTaken(long todayTaken) { dto.todayTaken = todayTaken; return this; }
        public DashboardDTOBuilder todayMissed(long todayMissed) { dto.todayMissed = todayMissed; return this; }
        public DashboardDTOBuilder totalMedicines(long totalMedicines) { dto.totalMedicines = totalMedicines; return this; }
        public DashboardDTOBuilder totalPrescriptions(long totalPrescriptions) { dto.totalPrescriptions = totalPrescriptions; return this; }
        public DashboardDTO build() { return dto; }
    }
}
