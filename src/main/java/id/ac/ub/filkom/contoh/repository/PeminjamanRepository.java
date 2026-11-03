package id.ac.ub.filkom.contoh.repository;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Peminjaman;
import id.ac.ub.filkom.contoh.entity.StatusPinjam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {
    long countByAnggotaAndStatus(Anggota anggota, StatusPinjam status);
    boolean existsByAnggotaAndBukuAndStatus(Anggota anggota, Buku buku, StatusPinjam status);
    List<Peminjaman> findByStatusOrderByTglPinjamDesc(StatusPinjam status);
    List<Peminjaman> findAllByOrderByIdDesc();
}