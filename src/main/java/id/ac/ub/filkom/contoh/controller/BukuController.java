package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.service.BukuService;
import id.ac.ub.filkom.contoh.exception.AturanBisnisException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Controller: terima request, panggil service, pilih template. */
@Controller
public class BukuController {

    private final BukuService bukuService;

    public BukuController(BukuService bukuService) {
        this.bukuService = bukuService;
    }

    // GET http://localhost:8081/buku?kata=java
    @GetMapping("/buku")
    public String daftarBuku(@RequestParam(required = false) String kata,
                             Model model) {
        model.addAttribute("daftarBuku", bukuService.cariBuku(kata));
        model.addAttribute("kata", kata);
        return "buku";               // -> templates/buku.html
    }

    // POST dari form tambah buku
    @PostMapping("/buku")
    public String tambahBuku(@RequestParam String idBuku,
                            @RequestParam String judul,
                            @RequestParam int stok,
                            RedirectAttributes redirect) {
        try {
            bukuService.tambahBuku(idBuku, judul, stok);
            redirect.addFlashAttribute("sukses", "Buku berhasil ditambahkan");
        } catch (AturanBisnisException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/buku";
    }
}
