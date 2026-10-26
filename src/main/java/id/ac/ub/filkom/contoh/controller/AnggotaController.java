
package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.service.AnggotaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AnggotaController {

    private final AnggotaService anggotaService;

    public AnggotaController(AnggotaService anggotaService) {
        this.anggotaService = anggotaService;
    }

    @GetMapping("/anggota")
    public String lihatAnggota(Model model) {
        model.addAttribute("daftarAnggota",
                anggotaService.lihatSemuaAnggota());
        return "anggota";
    }

    @PostMapping("/anggota/tambah")
    public String simpanAnggota(Anggota anggota) {
        anggotaService.tambahAnggota(anggota);
        return "redirect:/anggota";
    }
}
