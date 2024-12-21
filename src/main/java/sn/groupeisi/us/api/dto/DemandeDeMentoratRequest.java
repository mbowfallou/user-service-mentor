package sn.groupeisi.us.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class DemandeDeMentoratRequest {
    private Long etudiantId;
    private Long mentorId;
    private List<Long> domaines; // Liste des IDs des domaines
}