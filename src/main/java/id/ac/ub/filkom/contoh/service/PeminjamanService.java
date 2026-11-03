package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Peminjaman;
import id.ac.ub.filkom.contoh.entity.StatusPinjam;
import id.ac.ub.filkom.contoh.exception.AturanBisnisException;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.PeminjamanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/** Service: aturan peminjaman dan pengembalian buku. */
@Service
public class PeminjamanService {

    private static final int LAMA_PINJAM_HARI = 7;

    private final PeminjamanRepository peminjamanRepository;
    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;

    public PeminjamanService(PeminjamanRepository peminjamanRepository,
                             AnggotaRepository anggotaRepository,
                             BukuRepository bukuRepository) {
        this.peminjamanRepository = peminjamanRepository;
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
    }

    @Transactional
    public Peminjaman pinjam(String idAnggota, String idBuku) {
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> new AturanBisnisException("Anggota tidak ditemukan"));
        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() -> new AturanBisnisException("Buku tidak ditemukan"));

        if (peminjamanRepository.existsByAnggotaAndBukuAndStatus(
                anggota, buku, StatusPinjam.DIPINJAM)) {
            throw new AturanBisnisException(
                    anggota.getNama() + " masih meminjam buku \"" + buku.getJudul() + "\"");
        }

        long aktif = peminjamanRepository.countByAnggotaAndStatus(
                anggota, StatusPinjam.DIPINJAM);
        if (aktif >= anggota.getBatasPinjam()) {
            throw new AturanBisnisException(
                    anggota.getNama() + " sudah mencapai batas pinjam ("
                            + anggota.getBatasPinjam() + " buku)");
        }

        buku.kurangiStok();            // melempar exception jika stok habis

        LocalDate hariIni = LocalDate.now();
        return peminjamanRepository.save(
                new Peminjaman(buku, anggota, hariIni, hariIni.plusDays(LAMA_PINJAM_HARI)));
    }

    @Transactional
    public Peminjaman kembalikan(Long idPeminjaman) {
        Peminjaman peminjaman = peminjamanRepository.findById(idPeminjaman)
                .orElseThrow(() -> new AturanBisnisException("Data peminjaman tidak ditemukan"));

        peminjaman.kembalikan();       // melempar exception jika sudah dikembalikan
        peminjaman.getBuku().tambahStok();
        return peminjaman;
    }

    @Transactional(readOnly = true)
    public List<Peminjaman> daftarAktif() {
        return peminjamanRepository.findByStatusOrderByTglPinjamDesc(StatusPinjam.DIPINJAM);
    }

    @Transactional(readOnly = true)
    public List<Peminjaman> riwayat() {
        return peminjamanRepository.findAllByOrderByIdDesc();
    }
}