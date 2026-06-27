package com.JIMS.integration.controller;

import com.JIMS.integration.config.EmailConfig;
import com.JIMS.integration.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import javax.sql.DataSource;

@CrossOrigin
@RestController
@RequestMapping("/api/ticket")
public class MaintenanceTicketController {

    @Autowired private MaintenanceTicketRepository         ticketRepo;
    @Autowired private MaintenanceTicketAssignRepository   assignRepo;
    @Autowired private MaintenanceWorkLogRepository        workLogRepo;
    @Autowired private MaintenanceNotificationRepository   notifRepo;
    @Autowired private MaintenanceTicketHistoryRepository  historyRepo;
    
    
    @Autowired
	@Qualifier("webDataSource")
    private DataSource webSmartDataSource;
    
    
	@Autowired
	private EmailConfig emailSender;
    

    @GetMapping("/employee/getEmployeesByLocation")
    public ResponseEntity<?> getEmployeesByLocation(
            @RequestParam("locid") Integer locid) {
 
        List<Map<String, Object>> dataList = new ArrayList<>();
 
        String sql =
            "SELECT " +
            "    EmpId, " +
            "    EmpCode, " +
            "    EmpCode + ' - ' + Name AS EmployeeDisplay, " +
            "    pertel AS telephone " +
            "FROM Employee " +
            "WHERE LeftDate IS NULL " +
            "  AND Locid = ? " +
            "ORDER BY Name";
 
        try (Connection connection = webSmartDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
 
            ps.setInt(1, locid);
            ResultSet rs = ps.executeQuery();
 
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("EmpId",           rs.getInt("EmpId"));
                row.put("EmpCode",         rs.getString("EmpCode"));
                row.put("EmployeeDisplay", rs.getString("EmployeeDisplay"));
                // Return empty string instead of null so JS comparison is simple
                String tel = rs.getString("telephone");
                row.put("telephone", tel != null ? tel.trim() : "");
                dataList.add(row);
            }
 
            return ResponseEntity.ok(dataList);
 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
 
    // =========================================================================
    // RAISE TICKET  (now accepts empCode, empName, mobileNo + multi whatBroke)
    // =========================================================================
 
    /**
     * Raises a new maintenance ticket.
     *
     * POST /api/ticket/raise
     *
     * New params vs original:
     *   empCode   – employee code selected from dropdown  (e.g. "EMP-001")
     *   empName   – display name from dropdown            (e.g. "EMP-001 - John Smith")
     *   mobileNo  – auto-filled or manually entered       (may be blank)
     *   whatBroke – comma-separated list of selected items (multi-select)
     */
//    @PostMapping("/raise")
//    public ResponseEntity<?> raiseTicket(
//            @RequestParam Integer factoryId,
//            @RequestParam Integer machineDescId,
//            @RequestParam Integer machineId,
//            @RequestParam String  whatBroke,        // comma-separated
//            @RequestParam String  problemDesc,
//            @RequestParam Integer reportedBy,
//            @RequestParam Integer assignorUserId,
//            @RequestParam(required = false, defaultValue = "") String empCode,
//            @RequestParam(required = false, defaultValue = "") String empName,
//            @RequestParam(required = false, defaultValue = "") String mobileNo) {
// 
//        Map<String, Object> response = new HashMap<>();
//        try {
//            Calendar c = Calendar.getInstance();
//            String ticketNo = String.format("TKT-%d%02d%02d-%03d",
//                    c.get(Calendar.YEAR),
//                    c.get(Calendar.MONTH) + 1,
//                    c.get(Calendar.DAY_OF_MONTH),
//                    (int)(Math.random() * 900) + 100);
// 
//            ticketRepo.raiseTicket(
//                ticketNo, factoryId, machineDescId, machineId,
//                whatBroke, problemDesc, reportedBy,
//                empCode, empName, mobileNo
//            );
// 
//            Integer ticketId = ticketRepo.getTicketIdByTicketNo(ticketNo);
// 
//            notifRepo.sendNotification(
//                ticketId, factoryId, assignorUserId,
//                "RAISED",
//                "New ticket " + ticketNo + " raised. What broke: " + whatBroke
//            );
//            historyRepo.logHistory(ticketId, factoryId, null, 1, reportedBy, "Ticket raised");
// 
//            response.put("status",   true);
//            response.put("ticketNo", ticketNo);
//            response.put("ticketId", ticketId);
//            response.put("message",  "Ticket raised successfully");
//            return ResponseEntity.ok(response);
// 
//        } catch (Exception e) {
//            response.put("status",  false);
//            response.put("message", e.getMessage());
//            return ResponseEntity.internalServerError().body(response);
//        }
//    }
    
    
    @PostMapping("/raise")
    public ResponseEntity<?> raiseTicket(
            @RequestParam Integer factoryId,
            @RequestParam Integer machineDescId,
            @RequestParam Integer machineId,
            @RequestParam String  whatBroke,
            @RequestParam String  problemDesc,
            @RequestParam Integer reportedBy,
            @RequestParam Integer assignorUserId,
            @RequestParam(required = false, defaultValue = "") String empCode,
            @RequestParam(required = false, defaultValue = "") String empName,
            @RequestParam(required = false, defaultValue = "") String mobileNo) {
 
        Map<String, Object> response = new HashMap<>();
        try {
            Calendar c = Calendar.getInstance();
            String ticketNo = String.format("TKT-%d%02d%02d-%03d",
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH) + 1,
                    c.get(Calendar.DAY_OF_MONTH),
                    (int)(Math.random() * 900) + 100);
 
            ticketRepo.raiseTicket(
                ticketNo, factoryId, machineDescId, machineId,
                whatBroke, problemDesc, reportedBy,
                empCode, empName, mobileNo
            );
 
            Integer ticketId = ticketRepo.getTicketIdByTicketNo(ticketNo);
 
            notifRepo.sendNotification(
                ticketId, factoryId, assignorUserId,
                "RAISED",
                "New ticket " + ticketNo + " raised. What broke: " + whatBroke
            );
            historyRepo.logHistory(ticketId, factoryId, null, 1, reportedBy, "Ticket raised");
 
            // ── Fetch machine names for the email (best-effort) ──────────────
            String machineTypeName = machineDescId.toString();   // fallback to ID
            String machineSubCode  = machineId.toString();       // fallback to ID
            try {
                Map<String, Object> detail = ticketRepo.getTicketDetail(ticketId);
                if (detail != null) {
                    Object mt = detail.get("MACHINE_DESCRIPTION");
                    Object ms = detail.get("MACHINE_SUBCODE");
                    if (mt != null) machineTypeName = mt.toString();
                    if (ms != null) machineSubCode  = ms.toString();
                }
            } catch (Exception ignored) { /* email will still use IDs as fallback */ }
 
            // ── Send notification email to assignor(s) ───────────────────────
            String sessionUsername = "User #" + reportedBy;   // replace if you have name in session/DB
            int emailStatus = emailSender.sendTicketRaisedEmail(
                ticketNo,
                machineTypeName,
                machineSubCode,
                whatBroke,
                problemDesc,
                sessionUsername,
                empCode,
                empName,
                mobileNo
            );
            // emailStatus == 0 means mail failed; ticket is still saved, so we
            // log but do NOT fail the whole request.
            if (emailStatus == 0) {
                System.err.println("[WARN] Ticket " + ticketNo + " saved but notification email failed.");
            }
 
            response.put("status",   true);
            response.put("ticketNo", ticketNo);
            response.put("ticketId", ticketId);
            response.put("message",  "Ticket raised successfully");
            return ResponseEntity.ok(response);
 
        } catch (Exception e) {
            response.put("status",  false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
 
    
    
    // ═══════════════════════════════════════════════════════════════════════
    // 2. GET ALL TICKETS (assignor dashboard)
    // GET /api/ticket/getAll?factoryId=
    // ═══════════════════════════════════════════════════════════════════════
//    @GetMapping("/getAll")
//    public ResponseEntity<?> getAllTickets(
//            @RequestParam Integer factoryId,
//            @RequestParam(defaultValue = "1")  Integer page,
//            @RequestParam(defaultValue = "10") Integer pageSize) {
//
//        int offset = (page - 1) * pageSize;
//
//        List<Map<String, Object>> tickets =
//                ticketRepo.getOpenTicketsForAssignorPaged(factoryId, offset, pageSize);
//
//        Integer totalCount = ticketRepo.countTicketsForAssignor(factoryId);
//        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
//
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (Map<String, Object> ticket : tickets) {
//            Map<String, Object> row = new HashMap<>(ticket);
//            Integer ticketId   = ((Number) row.get("TICKET_ID")).intValue();
//            Integer machineDes = ((Number) row.get("MACHINE_DESC_ID")).intValue();
//            row.put("assigns", assignRepo.getAssignmentsForTicketshow(ticketId, machineDes));
//            result.add(row);
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("tickets",     result);
//        response.put("totalCount",  totalCount);
//        response.put("totalPages",  totalPages);
//        response.put("currentPage", page);
//        response.put("pageSize",    pageSize);
//
//        return ResponseEntity.ok(response);
//    }
    
    
    
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTickets(
            @RequestParam Integer factoryId,
            @RequestParam(defaultValue = "1")  Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        int offset = (page - 1) * pageSize;

        List<Map<String, Object>> tickets =
                ticketRepo.getOpenTicketsForAssignorPaged(factoryId, offset, pageSize);

        Integer totalCount = ticketRepo.countTicketsForAssignor(factoryId);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> ticket : tickets) {
            Map<String, Object> row = new HashMap<>(ticket);
            Integer ticketId   = ((Number) row.get("TICKET_ID")).intValue();
            Integer machineDes = ((Number) row.get("MACHINE_DESC_ID")).intValue();
            row.put("assigns", assignRepo.getAssignmentsForTicketshow(ticketId, machineDes));
            result.add(row);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("tickets",     result);
        response.put("totalCount",  totalCount);
        response.put("totalPages",  totalPages);
        response.put("currentPage", page);
        response.put("pageSize",    pageSize);

        return ResponseEntity.ok(response);
    }
    // ═══════════════════════════════════════════════════════════════════════
    // 3. GET WORKERS (for assign modal dropdown)
    // GET /api/ticket/getWorkers?factoryId=
    // ═══════════════════════════════════════════════════════════════════════
    @GetMapping("/getWorkers")
    public ResponseEntity<?> getWorkers(@RequestParam String factoryId,@RequestParam String machineCategoryId,@RequestParam String ticket_id) {
        try {
            return ResponseEntity.ok(ticketRepo.getWorkersByFactory(factoryId,machineCategoryId,ticket_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 4. MY TICKETS (ticket raiser)
    // GET /api/ticket/myTickets?factoryId=&userId=
    // ═══════════════════════════════════════════════════════════════════════
    @GetMapping("/myTickets")
    public ResponseEntity<?> getMyTickets(
            @RequestParam Integer factoryId,
            @RequestParam Integer userId) {
        return ResponseEntity.ok(ticketRepo.getTicketsByUser(factoryId, userId));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 5. TICKET DETAIL + TIMELINE
    // GET /api/ticket/detail?ticketId=
    // ═══════════════════════════════════════════════════════════════════════
    @GetMapping("/detail")
    public ResponseEntity<?> getTicketDetail(@RequestParam Integer ticketId) {
        Map<String, Object> result = new HashMap<>();
        
        
        
        Map<String, Object> ticket = ticketRepo.getTicketDetail(ticketId);

        result.put("ticket", ticket);

        // 2. Extract required fields
        Integer machineDescId = (Integer) ticket.get("MACHINE_DESC_ID");
        String machineDesc = (String) ticket.get("MACHINE_DESCRIPTION");
        
        
        result.put("history", historyRepo.getHistoryForTicket(ticketId));
        result.put("assigns", assignRepo.getAssignmentsForTicketgetdetail(ticketId,machineDescId));
        result.put("logs",    workLogRepo.getLogsForTicketgetDetils(ticketId,machineDescId));
        return ResponseEntity.ok(result);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 6. ASSIGN TICKET TO WORKER(S)
    // POST /api/ticket/assign
    // ═══════════════════════════════════════════════════════════════════════
    @PostMapping("/assign")
    public ResponseEntity<?> assignTicket(
            @RequestParam Integer         ticketId,
            @RequestParam Integer         factoryId,
            @RequestParam List<Integer>   workerIds,
            @RequestParam Integer         assignedBy,
            @RequestParam(required = false) String assignNote,
            @RequestParam Integer         reportedBy) {
 
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> ticketDetail = ticketRepo.getTicketDetail(ticketId);
            Integer oldStatus = ticketDetail != null
                    ? (Integer) ticketDetail.get("STATUS") : 1;
 
            // Convenience vars from ticket detail
            String ticketNo    = ticketDetail != null ? String.valueOf(ticketDetail.get("TICKET_NO"))    : "N/A";
            String machineType = ticketDetail != null ? String.valueOf(ticketDetail.get("MACHINE_DESCRIPTION")) : "";
            String machineName = ticketDetail != null ? String.valueOf(ticketDetail.get("MACHINE_SUBCODE"))     : "";
            String whatBroke   = ticketDetail != null ? String.valueOf(ticketDetail.get("WHAT_BROKE"))   : "";
            String problemDesc = ticketDetail != null ? String.valueOf(ticketDetail.get("PROBLEM_DESC")) : "";
 
            for (Integer workerId : workerIds) {
 
                // ── DB assign + in-app notification (unchanged) ──────────────
                assignRepo.assignTicket(ticketId, factoryId, workerId, assignedBy, assignNote);
 
                String notifMsg = ticketDetail != null
                    ? "Ticket " + ticketNo + " assigned to you. What broke: " + whatBroke
                    : "A new ticket has been assigned to you.";
                notifRepo.sendNotification(ticketId, factoryId, workerId, "ASSIGNED", notifMsg);
 
                // ── Send email to the assigned worker ────────────────────────
                try {
                    Map<String, Object> workerInfo = ticketRepo.getWorkerEmailByUserId(workerId);
 
                    if (workerInfo != null && workerInfo.get("worker_email") != null) {
                        String workerEmail = workerInfo.get("worker_email").toString().trim();
                        String workerName  = workerInfo.get("employee_display_name") != null
                                           ? workerInfo.get("employee_display_name").toString()
                                           : "Team Member";
 
                        if (!workerEmail.isEmpty()) {
                            int emailStatus = emailSender.sendTicketAssignedEmail(
                                workerEmail,
                                workerName,
                                ticketNo,
                                machineType,
                                machineName,
                                whatBroke,
                                problemDesc,
                                assignNote
                            );
                            if (emailStatus == 0) {
                                System.err.println("[WARN] Email failed for worker ID " + workerId
                                        + " (" + workerEmail + ") on ticket " + ticketNo);
                            }
                        } else {
                            System.err.println("[INFO] No email address found for worker ID " + workerId);
                        }
                    } else {
                        System.err.println("[INFO] Worker mapping not found for worker ID " + workerId);
                    }
                } catch (Exception emailEx) {
                    // Email failure must never block the ticket assignment
                    System.err.println("[WARN] Worker email lookup/send failed for ID "
                            + workerId + ": " + emailEx.getMessage());
                }
                // ────────────────────────────────────────────────────────────
            }
 
            ticketRepo.updateTicketStatus(ticketId, 2, assignedBy);
            historyRepo.logHistory(ticketId, factoryId, oldStatus, 2, assignedBy,
                    "Ticket assigned to " + workerIds + " worker(s)");
 
            // Notify ticket raiser (unchanged)
            String raiserMsg = "Your ticket " + ticketNo + " has been assigned. Work will begin shortly.";
            notifRepo.sendNotification(ticketId, factoryId, reportedBy, "ASSIGNED", raiserMsg);
 
            response.put("status",  true);
            response.put("message", "Ticket assigned to " + workerIds.size() + " worker(s)");
            return ResponseEntity.ok(response);
 
        } catch (Exception e) {
            response.put("status",  false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    @PostMapping("/startWork")
    public ResponseEntity<?> startWork(
            @RequestParam Integer ticketId,
            @RequestParam Integer assignId,
            @RequestParam Integer factoryId,
            @RequestParam String  workerId,    // ← String EmpCode
            @RequestParam Integer reportedBy) {
 
        Map<String, Object> response = new HashMap<>();
        try {
            workLogRepo.startWork(ticketId, assignId, factoryId, workerId);
            Integer logId = workLogRepo.getLatestLogId(assignId, workerId);
 
            assignRepo.updateAssignStatus(assignId, 2, 0);  // 0 = system (no int userId for emp)
 
            Map<String, Object> ticketDetail = ticketRepo.getTicketDetail(ticketId);
            Integer oldStatus = ticketDetail != null ? (Integer) ticketDetail.get("STATUS") : 2;
            ticketRepo.updateTicketStatus(ticketId, 3, 0);
            historyRepo.logHistory(ticketId, factoryId, oldStatus, 3, 0, "Worker " + workerId + " started work");
 
            String ticketNo = ticketDetail != null ? (String) ticketDetail.get("TICKET_NO") : "";
            notifRepo.sendNotification(ticketId, factoryId, reportedBy,
                    "STARTED", "Work has started on your ticket " + ticketNo);
 
            response.put("status",  true);
            response.put("logId",   logId);
            response.put("message", "Work started");
            return ResponseEntity.ok(response);
 
        } catch (Exception e) {
            response.put("status",  false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
 
    // ── 8. PAUSE WORK  (workerId = EmpCode String) ────────────────────────
    @PostMapping("/pauseWork")
    public ResponseEntity<?> pauseWork(
            @RequestParam Integer logId,
            @RequestParam String  workerId) {    // ← String EmpCode
 
        Map<String, Object> response = new HashMap<>();
        try {
            workLogRepo.pauseWork(logId, workerId);
            response.put("status",  true);
            response.put("message", "Work paused");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status",  false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
 
    // ── 9. RESUME WORK  (workerId = EmpCode String) ───────────────────────
    @PostMapping("/resumeWork")
    public ResponseEntity<?> resumeWork(
            @RequestParam Integer logId,
            @RequestParam String  workerId) {    // ← String EmpCode
 
        Map<String, Object> response = new HashMap<>();
        try {
            workLogRepo.resumeWork(logId, workerId);
            response.put("status",  true);
            response.put("message", "Work resumed");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status",  false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
 

 
    // ── 11. WORKER MARKS FIXED  (workerId = EmpCode String) ──────────────
//    @PostMapping("/markFixed")
//    public ResponseEntity<?> markFixed(
//            @RequestParam Integer logId,
//            @RequestParam Integer assignId,
//            @RequestParam Integer ticketId,
//            @RequestParam Integer factoryId,
//            @RequestParam String  workerId,    // ← String EmpCode
//            @RequestParam String  whatFixed,
//            @RequestParam(required = false) String remarks,
//            @RequestParam Integer assignedBy) {
// 
//        Map<String, Object> response = new HashMap<>();
//        try {
//            workLogRepo.markFixed(logId, whatFixed, remarks != null ? remarks : "", workerId);
//            assignRepo.updateAssignStatus(assignId, 3, 0);
// 
//            int notFixed = assignRepo.countNotFixedAssignments(ticketId);
//            Map<String, Object> ticketDetail = ticketRepo.getTicketDetail(ticketId);
//            Integer oldStatus = ticketDetail != null ? (Integer) ticketDetail.get("STATUS") : 3;
// 
//            if (notFixed == 0) {
//                ticketRepo.updateTicketStatus(ticketId, 4, 0);
//                historyRepo.logHistory(ticketId, factoryId, oldStatus, 4, 0,
//                        "All workers marked fixed. Pending closure.");
//            }
// 
//            String ticketNo = ticketDetail != null ? (String) ticketDetail.get("TICKET_NO") : "";
//            notifRepo.sendNotification(ticketId, factoryId, assignedBy,
//                    "FIXED", "Worker " + workerId + " has marked ticket " + ticketNo +
//                             " as fixed. Please verify and close.");
// 
//            response.put("status",  true);
//            response.put("message", "Marked as fixed. Assignor notified.");
//            return ResponseEntity.ok(response);
// 
//        } catch (Exception e) {
//            response.put("status",  false);
//            response.put("message", e.getMessage());
//            return ResponseEntity.internalServerError().body(response);
//        }
//    }
    
    
    @PostMapping("/markFixed")
    public ResponseEntity<?> markFixed(@RequestBody Map<String, Object> body) {

        Map<String, Object> response = new HashMap<>();
        try {
            Integer logId      = (Integer) body.get("logId");
            Integer assignId   = (Integer) body.get("assignId");
            Integer ticketId   = (Integer) body.get("ticketId");
            Integer factoryId  = (Integer) body.get("factoryId");
            String  workerId   = (String)  body.get("workerId");
            String  whatFixed  = (String)  body.get("whatFixed");
            String  remarks    = body.get("remarks") != null ? (String) body.get("remarks") : "";
            Integer assignedBy = (Integer) body.get("assignedBy");
            Integer partsReplaced = body.get("partsReplaced") != null ? (Integer) body.get("partsReplaced") : 0;

            // 1. Mark log fixed
            workLogRepo.markFixed(logId, whatFixed, remarks, workerId);

            // 2. Update assign status
            assignRepo.updateAssignStatus(assignId, 3, 0);

            // 3. Save parts used if any
            if (partsReplaced == 1) {
                List<Map<String, Object>> parts = (List<Map<String, Object>>) body.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    for (Map<String, Object> part : parts) {
                        workLogRepo.savePartsUsed(
                            ticketId, logId, assignId, workerId, factoryId,
                            part.get("itemId").toString(),
                            part.get("itemName").toString(),
                            Double.parseDouble(part.get("qty").toString())
                        );
                    }
                }
            }

            // 4. Check if all workers done
            int notFixed = assignRepo.countNotFixedAssignments(ticketId);
            Map<String, Object> ticketDetail = ticketRepo.getTicketDetail(ticketId);
            Integer oldStatus = ticketDetail != null ? (Integer) ticketDetail.get("STATUS") : 3;

            if (notFixed == 0) {
                ticketRepo.updateTicketStatus(ticketId, 4, 0);
                historyRepo.logHistory(ticketId, factoryId, oldStatus, 4, 0,
                        "All workers marked fixed. Pending closure.");
            }

            String ticketNo = ticketDetail != null ? (String) ticketDetail.get("TICKET_NO") : "";
            notifRepo.sendNotification(ticketId, factoryId, assignedBy,
                    "FIXED", "Worker " + workerId + " has marked ticket " + ticketNo +
                             " as fixed. Please verify and close.");

            response.put("status",  true);
            response.put("message", "Marked as fixed. Assignor notified.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status",  false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
 

    
    @GetMapping("/machineItems")
    public ResponseEntity<?> getMachineItems(
            @RequestParam Integer ticketId,
            @RequestParam Integer factoryId) {
        try {
        	Integer machineId = workLogRepo.getMachineIdByTicketId(ticketId);
        	

List<Map<String, Object>> items =
        workLogRepo.getMachineItemsByMachineId(machineId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    

    // ═══════════════════════════════════════════════════════════════════════
    // 12. ASSIGNOR CLOSES TICKET
    // POST /api/ticket/close
    // ═══════════════════════════════════════════════════════════════════════
    @PostMapping("/close")
    public ResponseEntity<?> closeTicket(
            @RequestParam Integer ticketId,
            @RequestParam Integer factoryId,
            @RequestParam Integer closedBy,
            @RequestParam Integer reportedBy,
            @RequestParam(required = false) String remarks) {

        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> ticketDetail = ticketRepo.getTicketDetail(ticketId);
            Integer oldStatus = ticketDetail != null ? (Integer) ticketDetail.get("STATUS") : 4;

            List<Map<String, Object>> logs = workLogRepo.getLogsForTicket(ticketId);
            for (Map<String, Object> log : logs) {
                Object statusObj = log.get("STATUS");
                if (statusObj != null && ((Number) statusObj).intValue() == 2) {
                    workLogRepo.closeLog(((Number) log.get("LOG_ID")).intValue(), closedBy);
                }
            }

            List<Map<String, Object>> assigns = assignRepo.getAssignmentsForTicket(ticketId);
            for (Map<String, Object> a : assigns) {
                Object aStatus = a.get("STATUS");
                if (aStatus != null && ((Number) aStatus).intValue() != 4) {
                    assignRepo.updateAssignStatus(((Number) a.get("ASSIGN_ID")).intValue(), 4, closedBy);
                }
            }

            ticketRepo.updateTicketStatusClosed(ticketId, 5, closedBy,remarks,String.valueOf(closedBy));
            historyRepo.logHistory(ticketId, factoryId, oldStatus, 5, closedBy,
                    remarks != null ? remarks : "Ticket closed by assignor");

            Object totalHours = workLogRepo.getTotalWorkHours(ticketId);
            List<Map<String, Object>> workerHours = workLogRepo.getWorkerHoursForTicket(ticketId);

            String ticketNo = ticketDetail != null ? (String) ticketDetail.get("TICKET_NO") : "";
            notifRepo.sendNotification(ticketId, factoryId, reportedBy,
                    "CLOSED", "Your ticket " + ticketNo + " has been successfully resolved and closed.");

            response.put("status",      true);
            response.put("message",     "Ticket closed successfully");
            response.put("totalHours",  totalHours);
            response.put("workerHours", workerHours);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status",  false);
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 13. WORKER'S TICKET LIST
    // GET /api/ticket/workerTickets?workerId=&factoryId=
    // ═══════════════════════════════════════════════════════════════════════
    @GetMapping("/activeLog")
    public ResponseEntity<?> getActiveLog(
            @RequestParam String  workerId,
            @RequestParam Integer factoryId) {
        try {
            List<Map<String, Object>> logs =
                workLogRepo.getActiveLogsForWorker(workerId, factoryId);
            Map<String, Object> result = new HashMap<>();
            result.put("activeLogs", logs != null ? logs : new ArrayList<>());
            result.put("serverTime", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "activeLogs", new ArrayList<>(),
                "serverTime", System.currentTimeMillis()
            ));
        }
    }
 
    // ── HEARTBEAT  (called every 2 min from JS) ───────────────────────────
    // Updates LAST_HEARTBEAT on all active/paused logs for this worker.
    // Lets any other device know the session is still live.
    @PostMapping("/heartbeat")
    public ResponseEntity<?> heartbeat(
            @RequestParam String  workerId,
            @RequestParam Integer factoryId) {
        try {
            workLogRepo.updateHeartbeat(workerId, factoryId);
            return ResponseEntity.ok(Map.of("status", true,
                                            "serverTime", System.currentTimeMillis()));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("status", false));
        }
    }
 
    // ── WORKER TICKETS  (workerId is String EmpCode) ─────────────────────
    // REPLACE the old Integer version with this String version:
    @GetMapping("/workerTickets")
    public ResponseEntity<?> getWorkerTickets(
            @RequestParam Integer  workerId,     // ← String EmpCode
            @RequestParam Integer factoryId) {
        return ResponseEntity.ok(assignRepo.getTicketsForWorker(workerId, factoryId));
    }
 
    // ═══════════════════════════════════════════════════════════════════════
    // 14. WORK HOURS SUMMARY
    // GET /api/ticket/workHours?ticketId=
    // ═══════════════════════════════════════════════════════════════════════
    @GetMapping("/workHours")
    public ResponseEntity<?> getWorkHours(@RequestParam Integer ticketId) {
        Map<String, Object> response = new HashMap<>();
        response.put("totalHours",  workLogRepo.getTotalWorkHours(ticketId));
        response.put("workerHours", workLogRepo.getWorkerHoursForTicket(ticketId));
        response.put("logs",        workLogRepo.getLogsForTicket(ticketId));
        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 15. NOTIFICATIONS
    // ═══════════════════════════════════════════════════════════════════════
    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(
            @RequestParam Integer userId,
            @RequestParam Integer factoryId) {
        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notifRepo.getNotificationsForUser(userId, factoryId));
        response.put("unreadCount",   notifRepo.countUnread(userId, factoryId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notifications/markRead")
    public ResponseEntity<?> markRead(@RequestParam Integer notifId) {
        notifRepo.markRead(notifId);
        return ResponseEntity.ok(Map.of("status", true));
    }

    @PostMapping("/notifications/markAllRead")
    public ResponseEntity<?> markAllRead(
            @RequestParam Integer userId,
            @RequestParam Integer factoryId) {
        notifRepo.markAllRead(userId, factoryId);
        return ResponseEntity.ok(Map.of("status", true));
    }
    
    
    @PostMapping("/employee/verifyEmpCode")
    public ResponseEntity<?> verifyEmpCode(
            @RequestParam String empCode,@RequestParam String FactoryId) {

        Map<String, Object> response = new HashMap<>();

        try {

            List<Object[]> workers =
            		workLogRepo.verifyWorker(empCode,FactoryId);

            if (workers != null && !workers.isEmpty()) {

                Object[] row = workers.get(0);

                response.put("valid", true);
                response.put("empCode", row[0]);
                response.put("empName", row[1]);

            } else {

                response.put("valid", false);
                response.put("empCode", "");
                response.put("empName", "");
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.put("valid", false);
            response.put("empCode", "");
            response.put("empName", "");
        }

        return ResponseEntity.ok(response);
    }
    
    
    
    
    @PostMapping("/transferTicket")
    public ResponseEntity<?> transferTicket(
            @RequestParam Integer ticketId,
            @RequestParam Integer assignId,
            @RequestParam(required = false) Integer logId,
            @RequestParam Integer factoryId,
            @RequestParam String workerId,
            @RequestParam String transferReason) {

        Map<String, Object> response = new HashMap<>();

        try {

            // 1. Update assignment as transfer requested
            assignRepo.transferAssignment(
                    assignId,
                    transferReason
            );

            // 2. Stop worker work log (if started)
            if (logId != null) {
                workLogRepo.transferWorkLog(logId);
            }

            // 3. History
            historyRepo.logHistory(
                    ticketId,
                    factoryId,
                    3,
                    3,
                    Integer.parseInt(workerId),
                    "Worker Transfer Requested : " + transferReason
            );

            // 4. Notify assignor
            notifRepo.sendNotification(
                    ticketId,
                    factoryId,
                    Integer.parseInt(workerId),
                    "TRANSFER_REQUEST",
                    "Worker " + workerId +
                    " requested transfer. Reason : " + transferReason
            );

            response.put("status", true);
            response.put("message", "Transfer request sent successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("status", false);
            response.put("message", e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }
    

}