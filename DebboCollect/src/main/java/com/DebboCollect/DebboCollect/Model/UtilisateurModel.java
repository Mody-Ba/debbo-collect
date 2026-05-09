package com.DebboCollect.DebboCollect.Model;

import com.DebboCollect.DebboCollect.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurModel {

    private Long id;

    private String nom;

    private String email;

    private String motDePasse;

    private Role role;

}
/*role de cette classe  utilisé dans service
logique métier c’est l’objet central métier entre :
Request
Entity
Response.*/