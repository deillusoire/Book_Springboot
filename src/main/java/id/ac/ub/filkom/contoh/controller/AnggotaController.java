
package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.service.AnggotaService;
import id.ac.ub.filkom.contoh.exception.AturanBisnisException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String simpanAnggota(Anggota anggota, RedirectAttributes redirect) {
        try {
            anggotaService.tambahAnggota(anggota);
            redirect.addFlashAttribute("sukses", "Anggota berhasil ditambahkan");
        } catch (AturanBisnisException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/anggota";
    }
}
