package id.ac.ub.filkom.contoh.service;

import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import org.springframework.stereotype.Service;
import id.ac.ub.filkom.contoh.exception.AturanBisnisException;

import java.util.List;

/** Service: tempat logika bisnis/aturan aplikasi. */
@Service
public class BukuService {

    private final BukuRepository bukuRepository;

    // Dependency injection lewat constructor
    public BukuService(BukuRepository bukuRepository) {
        this.bukuRepository = bukuRepository;
    }

    public List<Buku> cariBuku(String kata) {
        if (kata == null || kata.isEmpty()) {
            return bukuRepository.findAll();
        }
        return bukuRepository.findByJudulContainingIgnoreCase(kata.trim());
    }

    public Buku tambahBuku(String idBuku, String judul, int stok) {
        String id = idBuku == null ? "" : idBuku.trim();
        String namaJudul = judul == null ? "" : judul.trim();

        if (id.isEmpty() || namaJudul.isEmpty()) {
            throw new AturanBisnisException("ID dan judul buku tidak boleh kosong");
        }
        if (stok < 0) {
            throw new AturanBisnisException("Stok tidak boleh negatif");
        }
        if (bukuRepository.existsById(id)) {
            throw new AturanBisnisException("ID buku " + id + " sudah dipakai");
        }
        return bukuRepository.save(new Buku(id, namaJudul, stok));
    }
}
