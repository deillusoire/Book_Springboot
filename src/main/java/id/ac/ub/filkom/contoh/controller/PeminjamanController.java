package id.ac.ub.filkom.contoh.controller;

import id.ac.ub.filkom.contoh.exception.AturanBisnisException;
import id.ac.ub.filkom.contoh.service.AnggotaService;
import id.ac.ub.filkom.contoh.service.BukuService;
import id.ac.ub.filkom.contoh.service.PeminjamanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PeminjamanController {

    private final PeminjamanService peminjamanService;
    private final AnggotaService anggotaService;
    private final BukuService bukuService;

    public PeminjamanController(PeminjamanService peminjamanService,
                                AnggotaService anggotaService,
                                BukuService bukuService) {
        this.peminjamanService = peminjamanService;
        this.anggotaService = anggotaService;
        this.bukuService = bukuService;
    }

    @GetMapping("/peminjaman")
    public String halamanPeminjaman(Model model) {
        model.addAttribute("daftarAnggota", anggotaService.lihatSemuaAnggota());
        model.addAttribute("daftarBuku", bukuService.cariBuku(null));
        model.addAttribute("daftarAktif", peminjamanService.daftarAktif());
        model.addAttribute("riwayat", peminjamanService.riwayat());
        return "peminjaman";
    }

    @PostMapping("/peminjaman/pinjam")
    public String pinjam(@RequestParam String idAnggota,
                         @RequestParam String idBuku,
                         RedirectAttributes redirect) {
        try {
            peminjamanService.pinjam(idAnggota, idBuku);
            redirect.addFlashAttribute("sukses", "Peminjaman berhasil dicatat");
        } catch (AturanBisnisException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/peminjaman";
    }

    @PostMapping("/peminjaman/{id}/kembali")
    public String kembalikan(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            peminjamanService.kembalikan(id);
            redirect.addFlashAttribute("sukses", "Buku berhasil dikembalikan");
        } catch (AturanBisnisException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/peminjaman";
    }
}